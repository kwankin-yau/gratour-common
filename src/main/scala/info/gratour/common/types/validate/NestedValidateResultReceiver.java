/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.types.validate;

public class NestedValidateResultReceiver extends AbstractValidateResultReceiver {

    protected String namespace;
    protected final ValidateResultReceiver outerReceiver;

    public NestedValidateResultReceiver(String namespace, ValidateResultReceiver outerReceiver) {
        this.namespace = namespace;
        this.outerReceiver = outerReceiver;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public NestedValidateResultReceiver namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public ValidateResultReceiver getOuterReceiver() {
        return outerReceiver;
    }

    @Override
    public void invalidField(String fieldName) {
        if (namespace != null && !namespace.isEmpty())
            fieldName = namespace + "." + fieldName;

        outerReceiver.invalidField(fieldName);
        error = true;
    }
}
