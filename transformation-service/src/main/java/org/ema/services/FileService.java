package org.ema.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ema.entities.File;
import org.ema.exceptions.FileNotFoundException;
import org.ema.interfaces.IFileService;
import org.ema.records.CreateFileRequest;
import org.ema.records.UpdateFileRequest;
import org.ema.repositories.IFileRepository;
import org.ema.utils.Snowflake;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService implements IFileService {
    private final IFileRepository fileRepository;
    private final Snowflake snowflake;

    @Override
    public File createFile(CreateFileRequest request) {
        Long fileId = snowflake.nextId();
        File file = new File();
        file.setId(fileId);
        file.setUserId(request.userId());
        file.setSourceFileId(request.sourceFileId());
        file.setMimeType(request.mimeType());
        file.setSourceType(request.sourceType());
        // TODO: add encryption logic for auth key
        file.setSourceFileAuthKey(request.sourceFileAuthKey());
        return fileRepository.save(file);
    }

    @Override
    public File updateFile(Long fileId, UpdateFileRequest request) {
        File file = fileRepository.findById(fileId).orElse(null);
        if (file == null) {
            throw new FileNotFoundException("file with name: " + fileId + " not found");
        }
        file.setSourceFileAuthKey(request.sourceFileAuthKey());
        file.markUpdated();
        return fileRepository.save(file);
    }

    @Override
    public File getFile(Long fileId) {
        File file = fileRepository.findById(fileId).orElse(null);
        if (file == null) {
            throw new FileNotFoundException("file with name: " + fileId + " not found");
        }
        return file;
    }
}
