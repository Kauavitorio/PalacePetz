package co.kaua.palacepetz.Methods;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

public abstract class MaskEditUtil {

    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_RG = "##.###.###-#";
    public static final String FORMAT_FONE = "(##) #####-####";
    public static final String FORMAT_CNPJ = "##.###.###/####-##";
    public static final String FORMAT_CEP = "#####-###";
    public static final String FORMAT_DATE = "##/##/####";
    public static final String FORMAT_DATETIME = "##:##  ##/##/##";
    public static final String FORMAT_HOUR = "##:##";
    public static final String FORMAT_NUMCARD = "#### #### #### ####";
    public static final String FORMAT_DATAVALI = "##/##";
    public static final String FORMAT_WEIGHT = "## KG";

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return
     */
    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = MaskEditUtil.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }
}