package at.thelegend27.timemanagementtool.HelperClasses;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

/**
 * Created by markusfriedl on 24/05/2017.
 */

public class EmailFieldValidatorHelper extends BaseValidatorHelper {
    public EmailFieldValidatorHelper(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = "Invalid Email Address";
        mEmptyMessage = "Missing Email Address";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }
}
