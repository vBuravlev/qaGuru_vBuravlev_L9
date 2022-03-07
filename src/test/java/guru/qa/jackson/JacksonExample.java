package guru.qa.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.domain.TeacherJackson;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JacksonExample {
    String pathFileJson = "src/test/resources/simple.json";

    @Test
    void parseJsonJackson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(pathFileJson);
            assertTrue(file.exists());
            TeacherJackson teacher = mapper.readValue(file, TeacherJackson.class);
            assertThat(teacher.name).isEqualTo("Dmitrii");
            assertThat(teacher.address.street).isEqualTo("Mira");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
