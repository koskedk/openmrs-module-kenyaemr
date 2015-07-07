package org.openmrs.module.kenyaemr.calculation.library.hiv.art;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.kenyaemr.calculation.library.models.Cd4ValueAndDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by codehub on 06/07/15.
 */
public class BaselineCd4CountAndDateCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap artInitiationDate = calculate(new InitialArtStartDateCalculation(), cohort, context);
        CalculationResultMap allCd4 = Calculations.allObs(Dictionary.getConcept(Dictionary.CD4_COUNT), cohort, context);

        for(Integer ptId: cohort) {
            Cd4ValueAndDate cd4ValueAndDate = null;
            Date artInitiationDt = EmrCalculationUtils.datetimeResultForPatient(artInitiationDate, ptId);
            ListResult listResult = (ListResult) allCd4.get(ptId);
            List<Obs> validCd4 = new ArrayList<Obs>();
            List<Obs> allCd4Obs = CalculationUtils.extractResultValues(listResult);
            if(allCd4Obs.size() > 0 && artInitiationDt != null) {
                for (Obs obs : allCd4Obs) {
                    if (daysBetween(obs.getObsDatetime(), artInitiationDt) <= 90) {
                        validCd4.add(obs);
                    }
                }
            }
            if(validCd4.size() > 0) {
                new Cd4ValueAndDate(validCd4.get(validCd4.size() - 1).getValueNumeric(), validCd4.get(validCd4.size() - 1).getObsDatetime());
            }

            ret.put(ptId, new SimpleResult(cd4ValueAndDate, this));


        }
        return ret;
    }


    private  int daysBetween(Date date1, Date date2) {
        DateTime d1 = new DateTime(date1.getTime());
        DateTime d2 = new DateTime(date2.getTime());
        return Days.daysBetween(d1, d2).getDays();
    }
}
