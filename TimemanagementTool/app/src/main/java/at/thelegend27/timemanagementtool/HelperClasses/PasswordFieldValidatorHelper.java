package at.thelegend27.timemanagementtool.HelperClasses;

import android.support.design.widget.TextInputLayout;

import at.thelegend27.timemanagementtool.R;

/**
 * Created by markusfriedl on 24/05/2017.
 */

public class PasswordFieldValidatorHelper extends BaseValidatorHelper {
    private int mMinLength;

    public PasswordFieldValidatorHelper(TextInputLayout errorContainer, int length) {
        super(errorContainer);
        mMinLength = length;
        mErrorMessage = mErrorContainer.getResources().getQuantityString(R.plurals.error_week_password, mMinLength, mMinLength);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence.length() >= mMinLength;
    }
}
