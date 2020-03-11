package br.com.calcard.android.app.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class CpfCnpjMaks {
    private static final String maskCNPJ = "##.###.###/####-##";
    private static final String maskCPF = "###.###.###-##";

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    public static TextWatcher insert(final TextInputLayout editText) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            String lastCpf = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = CpfCnpjMaks.unmask(s.toString());
                if (!lastCpf.equals(str)) {
                    lastCpf = str;
                    String mask;
                    String defaultMask = getDefaultMask(str);
                    switch (str.length()) {
                        case 11:
                            mask = maskCPF;
                            break;
                        case 14:
                            mask = maskCNPJ;
                            break;

                        default:
                            mask = defaultMask;
                            break;
                    }

                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                            mascara += m;
                            continue;
                        }

                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    if (editText.getEditText() != null) {
                        editText.getEditText().setText(mascara);
                        editText.getEditText().setSelection(mascara.length());
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                editText.setError(null);
            }
        };
    }

    private static String getDefaultMask(String str) {
        String defaultMask = maskCPF;
        if (str.length() > 11) {
            defaultMask = maskCNPJ;
        }
        return defaultMask;
    }
}