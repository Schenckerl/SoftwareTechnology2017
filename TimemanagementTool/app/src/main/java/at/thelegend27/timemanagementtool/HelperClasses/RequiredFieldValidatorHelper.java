package at.thelegend27.timemanagementtool.HelperClasses;

import android.support.design.widget.TextInputLayout;

import at.thelegend27.timemanagementtool.R;

/**
 * Created by markusfriedl on 24/05/2017.
 */

public class RequiredFieldValidatorHelper extends BaseValidatorHelper {
    public RequiredFieldValidatorHelper(TextInputLayout errorContainer) {
        super(errorContainer);
        mEmptyMessage = errorContainer.getResources().getString(R.string.error_field_required);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }
}
