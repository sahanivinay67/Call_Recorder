package com.example.dailapp;

public class NumberFormatter {
    private String mRawNumber;
    private String mNumberFormatted;

    public NumberFormatter(String mNumberFormatted) {
        this.mNumberFormatted = mNumberFormatted;

        this.mRawNumber = mNumberFormatted.replace(" ", "");
        this.mRawNumber = mRawNumber.replace("-", "");
        this.mRawNumber = mRawNumber.replace("(", "");
        this.mRawNumber = mRawNumber.replace(")", "");
    }

    public String getmRawNumber() {
        return mRawNumber;
    }

    public void setmRawNumber(String mRawNumber) {
        this.mRawNumber = mRawNumber;
    }

    public String getmNumberFormatted() {
        formatNumber();

        return mNumberFormatted;
    }

    public void setmNumberFormatted(String mNumberFormatted) {
        this.mNumberFormatted = mNumberFormatted;
    }

    private void formatNumber(){
        boolean isCellphone = mRawNumber.length() == 9;
        boolean isHomephone = mRawNumber.length() == 8;
        Integer substringBreak = 0;

        if (isCellphone) substringBreak = 5;
        if (isHomephone) substringBreak = 4;

        if (isCellphone || isHomephone) {
            mNumberFormatted = mNumberFormatted.replace("-", "");
            mNumberFormatted = mNumberFormatted.substring(0, substringBreak) + "-" +
                    mNumberFormatted.substring(substringBreak, mNumberFormatted.length());
        }
    }
}
