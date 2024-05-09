package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class LabelInitializer implements ApplicationRunner {

    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws DataIntegrityViolationException {
        Label feature = new Label();
        feature.setName("feature");

        Label bug = new Label();
        bug.setName("bug");

        labelRepository.save(feature);
        labelRepository.save(bug);
    }
}