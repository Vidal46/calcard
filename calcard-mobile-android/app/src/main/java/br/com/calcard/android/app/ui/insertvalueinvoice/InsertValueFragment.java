package br.com.calcard.android.app.ui.insertvalueinvoice;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.InsertValueFragmentBinding;
import br.com.calcard.android.app.ui.invoicepayment.InvoicePayment;
import br.com.calcard.android.app.utils.CurrencyHelper;

public class InsertValueFragment extends Fragment {

    private InsertValueViewModel mViewModel;
    private InsertValueFragmentBinding binding;
    private Context context;
    private Double value;
    private Double totalValue;
    private Double minimumValue;
    private String dueDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.insert_value_fragment, container, false);
        context = inflater.getContext();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InsertValueViewModel.class);
        binding.setViewModel(mViewModel);
        binding.executePendingBindings();
        Bundle bundle = getArguments();
        totalValue = Double.parseDouble(String.valueOf(Objects.requireNonNull(bundle).getDouble("totalValue")));
        minimumValue = Double.parseDouble(String.valueOf(Objects.requireNonNull(bundle).getDouble("minimumValue")));
        dueDate = String.valueOf(Objects.requireNonNull(bundle).getString("dueDate"));
        binding.getViewModel().getInstallmentInvoice(dueDate);
        binding.btnTotalValue.setText(CurrencyHelper.format(totalValue));
        binding.btnMinimumValue.setText(CurrencyHelper.format(minimumValue));
        binding.btnTotalValue.setOnClickListener(v -> {
            binding.edtValue.setText(CurrencyHelper.format(totalValue));
        });
        binding.btnMinimumValue.setOnClickListener(v -> {
            binding.edtValue.setText(CurrencyHelper.format(minimumValue));
        });
        binding.btnPayInvoice.setEnabled(false);
        binding.edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    binding.edtValue.removeTextChangedListener(this);
                    String text = s.toString();
                    text = text.replace(".", "").replace(" ", "")
                            .replace(",", "")
                            .replace("R", "")
                            .replace("$", "").trim();
                    text = text.replaceAll("\\s+", "");
                    double parsed = Double.parseDouble(text);
                    value = parsed / 100;
                    binding.edtValue.setText(CurrencyHelper.format(value));
                    binding.edtValue.setSelection(binding.edtValue.getText().length());
                    binding.edtValue.addTextChangedListener(this);
                    if (value > totalValue) {
                        binding.edtValue.setError("Valor maior que o total da fatura.");
                        binding.btnPayInvoice.setEnabled(false);
                    } else if (value < minimumValue) {
                        binding.edtValue.setError("Valor menor que o mínimo da fatura.");
                        binding.btnPayInvoice.setEnabled(false);
                    } else {
                        binding.edtValue.setError(null);
                        binding.btnPayInvoice.setEnabled(true);
                    }
                    binding.btnPayInvoice.requestFocus();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnPayInvoice.setOnClickListener(v -> {
            Fragment fragment = new InvoicePayment();
            bundle.putDouble("amount", value);
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                    .replace(R.id.fragment_container,
//                            fragment).addToBackStack(null)
//                    .commit();
        });
        binding.btnInstallmentnvoice.setOnClickListener(v -> {
//            Fragment fragment = new InvoiceInstallmentFragment();
//            fragment.setArguments(bundle);
//            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container,
//                            fragment).addToBackStack(null)
//                    .commit();
        });
    }
}
