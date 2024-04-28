package org.ema.interfaces;

import org.ema.records.FileKafkaMessage;

public interface IDownloadStrategy {
    // implement download method basis mime type
    byte[] download(FileKafkaMessage fileKafkaMessage);
}
