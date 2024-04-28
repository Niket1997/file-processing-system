package org.ema.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.enums.TransformationStatus;
import org.ema.external.transformationservice.TransformationServiceClient;
import org.ema.interfaces.ITransformationStrategy;
import org.ema.records.TransformationKafkaMessage;
import org.ema.records.UpdateTransformationRequest;
import org.ema.transformations.SimpleTransformationFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpleTransformationFactory transformationFactory;
    private final TransformationServiceClient transformationServiceClient;

    @KafkaListener(topics = "#{@environment.getProperty('spring.kafka.topics').split(',')}", groupId = "${spring.kafka.group-id}")
    public void consumer(TransformationKafkaMessage transformation) {
        log.info(transformation.toString());

        // get appropriate transformer
        ITransformationStrategy transformationStrategy = transformationFactory.getTransformationStrategy(transformation.transformationName());

        // perform transformation
        Long outputFileId = transformationStrategy.transform(transformation);

        // update transformation
        UpdateTransformationRequest updateTransformationRequest = new UpdateTransformationRequest(TransformationStatus.COMPLETED, outputFileId);
        transformationServiceClient.updateTransformation(transformation.transformationId(), updateTransformationRequest);
        log.info("updated transformation with id {}", outputFileId);
    }
}
