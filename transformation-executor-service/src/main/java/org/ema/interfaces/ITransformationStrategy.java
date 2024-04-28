package org.ema.interfaces;

import org.ema.records.TransformationKafkaMessage;

public interface ITransformationStrategy {
    Long transform(TransformationKafkaMessage transformationKafkaMessage);
}
