package org.ema.transformations;

import lombok.RequiredArgsConstructor;
import org.ema.enums.TransformationName;
import org.ema.interfaces.ITransformationStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleTransformationFactory {
    private final CapitalizeFileContentTransformation capitalizeFileContentTransformation;

    public ITransformationStrategy getTransformationStrategy(String transformationName) {
        // if number of transformations increase, then we can use switch case here
        if (transformationName.equals(TransformationName.CAPITALIZE_FILE.getValue())) {
            return capitalizeFileContentTransformation;
        }
        throw new IllegalArgumentException("Unknown transformation name: " + transformationName);
    }
}
