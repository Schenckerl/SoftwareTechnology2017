package at.thelegend27.timemanagementtool.HelperClasses;

import android.support.design.widget.TextInputLayout;

/**
 * Created by markusfriedl on 24/05/2017.
 */

public class RequiredFieldValidatorHelper extends BaseValidatorHelper {
    public RequiredFieldValidatorHelper(TextInputLayout errorContainer) {
        super(errorContainer);
        mEmptyMessage = "This Field is required";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }
}
