package comp3350.a15.eventease.logic.exceptions;

import androidx.annotation.Nullable;

public class EmptyVendorCategoryException extends VendorException {
    @Nullable
    @Override
    public String getMessage() {
        return "There are no vendors providing the selected service(s) at this time";
    }
}
