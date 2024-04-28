package org.ema.interfaces;

import org.ema.entities.File;
import org.ema.records.CreateFileRequest;
import org.ema.records.UpdateFileRequest;

public interface IFileService {
    File createFile(CreateFileRequest request);

    File updateFile(Long fileId, UpdateFileRequest request);

    File getFile(Long fileId);
}
