package infsus.pampol.delegate;

import infsus.pampol.service.DoctorService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("getAllDoctorsDelegate")
public class GetAllDoctorsDelegate implements JavaDelegate {

    @Autowired
    private DoctorService doctorService;

    @Override
    public void execute(DelegateExecution execution) {
        execution.setVariable("doctors", doctorService.getAllDoctors());
    }
}
