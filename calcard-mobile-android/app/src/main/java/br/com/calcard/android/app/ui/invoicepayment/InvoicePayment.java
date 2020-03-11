package br.com.calcard.android.app.ui.invoicepayment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.InvoicePaymentFragmentBinding;
import br.com.calcard.android.app.utils.CurrencyHelper;


public class InvoicePayment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-mm-dd");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat outSDF = new SimpleDateFormat("dd/mm");
    private InvoicePaymentViewModel mViewModel;
    private InvoicePaymentFragmentBinding binding;
    private Context context;
    private ProgressDialog progressDialog;
    private Double totalValue;
    private String dueDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.invoice_payment_fragment, container, false);
        context = inflater.getContext();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InvoicePaymentViewModel.class);
        binding.setViewModel(mViewModel);
        binding.executePendingBindings();
        Bundle bundle = getArguments();

        totalValue = Double.parseDouble(String.valueOf(Objects.requireNonNull(bundle).getDouble("totalValue")));
        dueDate = String.valueOf(Objects.requireNonNull(bundle).getString("dueDate"));

        boolean progress = binding.getViewModel().loadingState.get();
        if (!progress) {
            progressDialog = ProgressDialog.show(getActivity(), "Gerando linha de pagamento",
                    "Aguarde...", true, false);
            binding.getViewModel().getBill(dueDate, totalValue.toString());
        }
        binding.getViewModel().mutableLoadingState.observe(this, view -> {
            progressDialog.dismiss();
            setValues();

        });

        error();
        binding.btnToShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, binding.getViewModel().bill.getDigitableLine().replace(".", "").replace(" ", ""));
            sendIntent.setType("text/plain");
            Intent chooser = Intent.createChooser(sendIntent, "Código para pagamento Calcard.");
            if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(chooser);
            }
        });
        binding.tvLinePayment.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(binding.getViewModel().bill.getDigitableLine().replace(".", "").replace(" ", ""));
            Toast.makeText(getActivity(), "Copiado para área de transferência.", Toast.LENGTH_SHORT).show();
        });

    }

    private void setValues() {
        String code = binding.getViewModel().bill.getDigitableLine();
        binding.tvDigitableLine.setText(code);
        binding.tvInvoiceAmount.append(CurrencyHelper.format(totalValue));
        binding.tvVenc.append(formatDate(dueDate));
        binding.tvValueInvoice.append(CurrencyHelper.format(totalValue));

    }

    public void error() {
        binding.getViewModel().billOk.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        Toast.makeText(getActivity(), binding.getViewModel().billOk.get(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String formatDate(String inDate) {
        String outDate = "";
        try {
            Date date = inSDF.parse(inDate);
            outDate = outSDF.format(date);
        } catch (ParseException ignored) {
            //IGNORED
        }
        return outDate;
    }

}
