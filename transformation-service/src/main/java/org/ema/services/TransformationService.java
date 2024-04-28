package org.ema.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.entities.File;
import org.ema.entities.Transformation;
import org.ema.entities.TransformationCategory;
import org.ema.enums.TransformationStatus;
import org.ema.exceptions.SignedURLGenerationFailedException;
import org.ema.exceptions.TransformationNotFoundException;
import org.ema.interfaces.IFileService;
import org.ema.interfaces.ITransformationCategoryService;
import org.ema.interfaces.ITransformationService;
import org.ema.records.*;
import org.ema.repositories.ITransformationRepository;
import org.ema.utils.CloudfrontSignedUrlGenerator;
import org.ema.utils.Snowflake;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransformationService implements ITransformationService {
    private final ITransformationRepository transformationRepository;
    private final ITransformationCategoryService transformationCategoryService;
    private final KafkaTemplate<String, TransformationKafkaMessage> kafkaTemplate;
    private final RedisTemplate<Integer, TransformationResponse> redisTemplate;
    private final CloudfrontSignedUrlGenerator cloudfrontSignedUrlGenerator;
    private final IFileService fileService;
    private final Snowflake snowflake;

    @Override
    public Transformation createTransformation(CreateTransformationRequest request) {
        // validate transformation request
        // if the transformation category is invalid, then following get will throw error
        TransformationCategory transformationCategory =
                transformationCategoryService.getTransformationCategory(request.transformationName());

        // create file
        File file = fileService.createFile(request.file());

        // create transformation request
        Transformation transformation = new Transformation();
        Long transformationId = snowflake.nextId();
        transformation.setId(transformationId);
        transformation.setTransformationStatus(TransformationStatus.CREATED);
        transformation.setFileId(file.getId());
        transformation.setTransformationName(transformationCategory.getName());
        transformation.setUserId(file.getUserId());

        // push message to Kafka
        String topicName = transformationCategory.getTransformationType().toString();
        log.info("pushed message to topic {}", topicName);
        kafkaTemplate.send(topicName, getTransformationKafkaMessage(transformation, file, transformationCategory));

        // return transformation entity
        return transformationRepository.save(transformation);
    }

    @Override
    public CreateTransformationsResponse createTransformations(List<CreateTransformationRequest> transformationRequests) {
        List<Transformation> successfulTransformations = new ArrayList<>();
        List<CreateTransformationRequest> failedTransformationRequests = new ArrayList<>();
        for (CreateTransformationRequest transformationRequest : transformationRequests) {
            try {
                Transformation transformation = createTransformation(transformationRequest);
                successfulTransformations.add(transformation);
            } catch (Exception e) {
                failedTransformationRequests.add(transformationRequest);
            }
        }
        return new CreateTransformationsResponse(
                successfulTransformations, failedTransformationRequests
        );
    }

    @Override
    public Transformation updateTransformation(Long transformationId, UpdateTransformationRequest request) {
        Transformation transformation = transformationRepository.findById(transformationId).orElse(null);
        if (transformation == null) {
            throw new TransformationNotFoundException("transformation with id: " + transformationId + " not found");
        }

        transformation.setTransformationStatus(request.transformationStatus());
        if (request.outputFileId() != 0) {
            transformation.setOutputFileId(request.outputFileId());
        }

        if (request.transformationStatus() == TransformationStatus.COMPLETED) {
            redisTemplate.convertAndSend(String.valueOf(transformation.getUserId()), getTransformationResponse(transformation));
        }
        return transformationRepository.save(transformation);
    }

    @Override
    public TransformationResponse getTransformation(Long transformationId) {
        Transformation transformation = transformationRepository.findById(transformationId).orElse(null);
        if (transformation == null) {
            throw new TransformationNotFoundException("transformation with id: " + transformationId + " not found");
        }
        return getTransformationResponse(transformation);
    }

    @Override
    public List<TransformationResponse> getTransformationForUser(Long userId) {
        List<Transformation> transformations = transformationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        List<TransformationResponse> transformationResponses = new ArrayList<>();
        for (Transformation transformation : transformations) {
            transformationResponses.add(getTransformationResponse(transformation));
        }
        return transformationResponses;
    }

    private TransformationResponse getTransformationResponse(Transformation transformation) {
        String cdnUrl = getCDNURL(transformation);
        return new TransformationResponse(
                transformation.getId(),
                transformation.getTransformationStatus(),
                transformation.getFileId(),
                transformation.getTransformationName(),
                transformation.getUserId(),
                transformation.getOutputFileId(),
                cdnUrl
        );
    }

    private String getCDNURL(Transformation transformation) {
        if (transformation.getTransformationStatus() != TransformationStatus.COMPLETED) {
            return "";
        }

        String key = "transformations/" + transformation.getUserId() + "/" + transformation.getOutputFileId();
        try {
            return cloudfrontSignedUrlGenerator.generateSignedUrlForResource(key);
        } catch (Exception e) {
            log.error(e.toString());
            throw new SignedURLGenerationFailedException("Could not generate signed cloudfront URL");
        }
    }

    private TransformationKafkaMessage getTransformationKafkaMessage(Transformation transformation, File file, TransformationCategory transformationCategory) {
        FileKafkaMessage fileKafkaMessage = new FileKafkaMessage(
                file.getId(),
                file.getUserId(),
                file.getSourceFileId(),
                file.getSourceType(),
                file.getMimeType(),
                file.getSourceFileAuthKey()
        );

        return new TransformationKafkaMessage(
                transformation.getId(),
                transformation.getTransformationStatus(),
                fileKafkaMessage,
                transformation.getTransformationName(),
                transformationCategory.getTransformationType(),
                transformation.getUserId()
        );
    }
}
