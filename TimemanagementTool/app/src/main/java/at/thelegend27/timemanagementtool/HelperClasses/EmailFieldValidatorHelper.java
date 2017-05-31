package at.thelegend27.timemanagementtool.HelperClasses;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import at.thelegend27.timemanagementtool.R;

/**
 * Created by markusfriedl on 24/05/2017.
 */

public class EmailFieldValidatorHelper extends BaseValidatorHelper {
    public EmailFieldValidatorHelper(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = errorContainer.getResources().getString(R.string.invalid_email_address);
        mEmptyMessage = errorContainer.getResources().getString(R.string.missing_email_address);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }
}
