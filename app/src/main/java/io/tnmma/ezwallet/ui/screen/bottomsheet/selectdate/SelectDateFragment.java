package io.tnmma.ezwallet.ui.screen.bottomsheet.selectdate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.databinding.FragmentSelectDateBinding;
import io.tnmma.ezwallet.ui.adapter.DateTypeAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.customview.DateRangeView;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class SelectDateFragment
        extends BaseBottomSheetFragment<FragmentSelectDateBinding, SelectDateViewModel> {

    private static final String TAG = SelectDateFragment.class.getSimpleName();

    private DateTypeAdapter dateTypeAdapter;

    private DatePickerDialog datePickerDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_date;
    }

    @Override
    protected Class<SelectDateViewModel> getViewModelClass() {
        return SelectDateViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        DateType dateType = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            dateType = (DateType) arguments.get(ConstantKeys.DateOption.TYPE);
            startDate = (LocalDate) arguments.get(ConstantKeys.DateOption.START_DATE);
            endDate = (LocalDate) arguments.get(ConstantKeys.DateOption.END_DATE);
        }
        return SelectDateViewModel.getFactory(dateType, startDate, endDate);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupDateTypeListUi();
        setupDateTypeAdapter();
        listenForDateRangeClick();
        listenForBottomActionClick();
    }

    private void setupDateTypeListUi() {
        int orientation = LinearLayout.HORIZONTAL;
        binding.fragmentSelectDateList.setItemAnimator(null);
        binding.fragmentSelectDateList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectDateList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectDateList.addItemDecoration(itemDecoration);
    }

    private void setupDateTypeAdapter() {
        dateTypeAdapter = new DateTypeAdapter();
        binding.fragmentSelectDateList.setAdapter(dateTypeAdapter);
        dateTypeAdapter
                .getSelectedDateTypeLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<DateType>() {
                            @Override
                            public void onChanged(DateType dateType) {
                                viewModel.setDateType(dateType);
                            }
                        });
        dateTypeAdapter.submitList(
                viewModel.getDateTypeList(),
                new Runnable() {
                    @Override
                    public void run() {
                        scrollToSelectedPosition();
                    }
                });
    }

    private void scrollToSelectedPosition() {
        int selectedPosition = dateTypeAdapter.getSelectedPosition();
        if (selectedPosition > -1) {
            binding.fragmentSelectDateList.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            ((CustomLinearLayoutManager)
                                            binding.fragmentSelectDateList.getLayoutManager())
                                    .smoothScrollAndCenter(selectedPosition);
                        }
                    });
        }
    }

    private void listenForDateRangeClick() {
        binding.fragmentSelectDateRange.setOnDateClickListener(
                new DateRangeView.OnDateClickListener() {
                    @Override
                    public void onStartDateClick() {
                        setupDatePickerDialog(
                                viewModel.getDateRange().getStartDate(),
                                null,
                                viewModel.getDateRange().getEndDate());
                        datePickerDialog.setOnDateSetListener(
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(
                                            DatePicker view, int year, int month, int dayOfMonth) {
                                        viewModel.setStartDate(
                                                LocalDate.of(year, month + 1, dayOfMonth));
                                    }
                                });
                        datePickerDialog.show();
                    }

                    @Override
                    public void onEndDateClick() {
                        setupDatePickerDialog(
                                viewModel.getDateRange().getEndDate(),
                                viewModel.getDateRange().getStartDate(),
                                null);
                        datePickerDialog.setOnDateSetListener(
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(
                                            DatePicker view, int year, int month, int dayOfMonth) {
                                        viewModel.setEndDate(
                                                LocalDate.of(year, month + 1, dayOfMonth));
                                    }
                                });
                        datePickerDialog.show();
                    }
                });
    }

    private void setupDatePickerDialog(
            LocalDate currentDate, LocalDate minDate, LocalDate maxDate) {
        datePickerDialog = new DatePickerDialog(getContext());
        if (currentDate != null) {
            datePickerDialog.updateDate(
                    currentDate.getYear(),
                    currentDate.getMonthValue() - 1,
                    currentDate.getDayOfMonth());
        }
        if (minDate != null) {
            datePickerDialog
                    .getDatePicker()
                    .setMinDate(minDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        if (maxDate != null) {
            datePickerDialog
                    .getDatePicker()
                    .setMaxDate(maxDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
    }

    private void listenForBottomActionClick() {
        binding.fragmentSelectDateBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        if (!validateConfirmButton()) {
                            showToast(R.string.error_custom_date_range_no_date_selected);
                            return;
                        }

                        Bundle result = new Bundle();
                        result.putSerializable(
                                ConstantKeys.DateOption.TYPE,
                                viewModel.getDateRange().getDateType());
                        result.putSerializable(
                                ConstantKeys.DateOption.START_DATE,
                                viewModel.getDateRange().getStartDate());
                        result.putSerializable(
                                ConstantKeys.DateOption.END_DATE,
                                viewModel.getDateRange().getEndDate());
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.DATE_OPTION, result);
                        dismiss();
                    }
                });
    }

    private boolean validateConfirmButton() {
        if (viewModel.getDateRange().getDateType() != DateType.CUSTOM_RANGE) {
            return true;
        }
        return (viewModel.getDateRange().getStartDate() != null
                || viewModel.getDateRange().getEndDate() != null);
    }
}
