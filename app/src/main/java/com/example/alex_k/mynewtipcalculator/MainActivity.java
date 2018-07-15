package com.example.alex_k.mynewtipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {



    // константы, используемые при сохранении/восстановлении состояния
    private static final String   BILL_TOTAL = "BILL_TOTAL";
    private static final String   CUSTOM_PERCENT = "CUSTOM_PERCENT";
    private              double   currentBillTotal;                               // счет, вводимый пользователем
    private              int      currentCustomPercent;                           // % чаевых, выбранный SeekBar
    private              EditText billEditText;                                   // ввод счета пользователем
    private              TextView customTipTextView;                              // % пользовательских чаевых
    private              EditText tipCustomEditText;                              // пользовательские чаевые
    private              EditText totalCustomEditText;                            // общий счет

    // Вызывается при первом создании класса activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Приложение запущено впервые или восстановлено из памяти?
        if ( savedInstanceState == null ) // приложение запущено впервые
        {
            currentBillTotal = 0.0;               // инициализация суммы счета нулем
            currentCustomPercent = 10;            // инициализация пользовательских чаевых значением 10%
        } // конец структуры if
        else // приложение восстановлено из памяти
        {
            // инициализация суммы счета сохраненной в памяти суммой
            currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);
            // инициализация пользовательских чаевых сохраненным процентом чаевых
            currentCustomPercent = savedInstanceState.getInt(CUSTOM_PERCENT);
        } // конец структуры else


        // TextView, отображающий процент пользовательских чаевых
        customTipTextView = (TextView) findViewById(R.id.customTipTextView);
        // пользовательские чаевые и итоговые EditTexts
        tipCustomEditText = (EditText) findViewById(R.id.tipCustomEditText);
        totalCustomEditText = (EditText) findViewById(R.id.totalCustomEditText);
        // получение billEditText
        billEditText = (EditText) findViewById(R.id.billEditText);
        //billEditText.setSelection(0);
        // billEditTextWatcher обрабатывает событие onTextChanged из billEditText
        billEditText.addTextChangedListener(billEditTextWatcher);
        // получение SeekBar, используемого для подсчета суммы пользовательских чаевых
        SeekBar customSeekBar = (SeekBar) findViewById(R.id.seekBar);
        customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);

    }
    // обновляет компоненты EditText, включающие пользовательские чаевые и итоги
    private void updateCustom()
    {
        // настройка текста customTipTextView в соответствии с положением SeekBar
        customTipTextView.setText(currentCustomPercent + " %");
        // вычисление суммы пользовательских чаевых
        double customTipAmount = currentBillTotal * currentCustomPercent * .01;
        // вычисление итогового счета, включая пользовательские чаевые
        double customTotalAmount = currentBillTotal + customTipAmount;
        // отображение суммы чаевых и итогового счета
        tipCustomEditText.setText(String.format(" %.02f", customTipAmount));
        totalCustomEditText.setText(String.format(" %.02f", customTotalAmount));
    } // конец метода updateCustom

    // сохранение значений billEditText и customSeekBar
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putDouble( BILL_TOTAL, currentBillTotal );
        outState.putInt( CUSTOM_PERCENT, currentCustomPercent );
    } // конец метода onSaveInstanceState

    // вызывается при изменении пользователем положения ползунка SeekBar
    private OnSeekBarChangeListener customSeekBarListener = new OnSeekBarChangeListener()
    {
        // обновление currentCustomPercent, потом вызов updateCustom
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            // присваивание currentCustomPercent положения ползунка SeekBar
            currentCustomPercent = seekBar.getProgress();
            // обновление EditTexts пользовательскими чаевыми и итоговыми счетами
            updateCustom();
        } // конец метода onProgressChanged

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
        } // конец метода onStartTrackingTouch
        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
        } // конец метода onStopTrackingTouch

    }; // конец OnSeekBarChangeListener

    // объект обработки событий, реагирующий на события billEditText
    private TextWatcher billEditTextWatcher = new TextWatcher()
    {
        // вызывается после ввода пользователем числа
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // преобразование текста billEditText в double
            try
            {
                currentBillTotal = Double.parseDouble(s.toString());
            } // завершение блока try

            catch (NumberFormatException e)
            {
                // по умолчанию в случае исключения
                currentBillTotal = 0.0;
            } // завершение блока catch

            updateCustom();   // обновление EditText с пользовательскими чаевыми
        } // конец метода onTextChanged

        @Override
        public void afterTextChanged(Editable s)
        {
        } // конец метода afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        } // завершение метода beforeTextChanged

    }; // завершение billEditTextWatcher

}
