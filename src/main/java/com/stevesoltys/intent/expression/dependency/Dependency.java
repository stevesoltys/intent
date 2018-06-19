package com.stevesoltys.intent.expression.dependency;

import com.stevesoltys.intent.expression.token.Token;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Steve Soltys
 */
@Data
public class Dependency implements Serializable {

    private static final long serialVersionUID = 0L;

    private final DependencyType dependencyType;

    private final Token governor;

    private final Token dependent;
}
