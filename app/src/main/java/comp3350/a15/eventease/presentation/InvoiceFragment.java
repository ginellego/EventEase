package comp3350.a15.eventease.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import comp3350.a15.eventease.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceFragment extends DialogFragment {
    private ParcelableInvoice invoice;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param invoice the invoice to be displayed as a fragment.
     * @return A new instance of fragment Invoice.
     */
    public static InvoiceFragment newInstance(ParcelableInvoice invoice) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putParcelable("invoice", invoice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ParcelableInvoice invParcel = getArguments().getParcelable("invoice", ParcelableInvoice.class);
            if (invParcel != null) {
                invoice = invParcel;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invoice, container, false);

        // Find TextViews and Button
        TextView serviceTypeTextView = v.findViewById(R.id.serviceTypeTextView);
        TextView nameOfEventTextView = v.findViewById(R.id.nameOfEventTextView);
        TextView dateOfEventTextView = v.findViewById(R.id.dateOfEventTextView);
        TextView timeOfEventTextView = v.findViewById(R.id.timeOfEventTextView);
        TextView locationOfEventTextView = v.findViewById(R.id.locationOfEventTextView);
        TextView offerAccepted = v.findViewById(R.id.offerAccepted);
        Button closeButton = v.findViewById(R.id.closeButton);

        serviceTypeTextView.setText(invoice.getServiceType());
        nameOfEventTextView.setText(invoice.getEventName());
        dateOfEventTextView.setText(invoice.getEventDate());
        timeOfEventTextView.setText(invoice.getEventTime());
        locationOfEventTextView.setText(invoice.getEventLocation());
        offerAccepted.setText("$" + invoice.getOfferAccepted());
        closeButton.setOnClickListener(v1 -> closeFragment());

        return v;
    }

    private void closeFragment() {
        // Close or dismiss the fragment
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}