package com.patient.records.service.image;

import com.patient.records.entity.PatientCard;
import com.patient.records.service.PatientCardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {

    static String RESOURCES = "src/main/resources/";
    static String IMAGES_PATH = RESOURCES + "images/";
    static String PATIENT_CARD_DIRECTORY = PatientCard.class.getSimpleName().toLowerCase(Locale.ROOT) + "/";

    PatientCardService patientCardService;

    public Resource getFile(@NotNull Long patientId) {
        var patientCard = patientCardService.getPatientCardById(patientId);

        if (Objects.isNull(patientCard.getPhoto()) || patientCard.getPhoto().length == 0) {
            throw new UnsupportedOperationException("Patient doesn't contain a photo");
        }

        var photoName = (String) SerializationUtils.deserialize(patientCard.getPhoto());
        var photo = Paths.get(getPathToPatientId(patientId) + photoName);

        try {
            return new UrlResource(photo.toUri());
        } catch (MalformedURLException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public void saveFile(@NotNull Long patientId, @NotNull MultipartFile multipartFile) {
        try {
            var path = createGeneralPath(patientId);
            var photoName = multipartFile.getOriginalFilename();

            FileUtils.cleanDirectory(new File(path));
            Path root = Paths.get(path);

            Files.copy(multipartFile.getInputStream(),
                    root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename())));

            patientCardService.setPhoto(SerializationUtils.serialize(photoName), patientId);
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static void deleteFile(@NotNull Long patientId) {
        try {
            FileUtils.cleanDirectory(new File(createGeneralPath(patientId)));
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static String createGeneralPath(Long patientId) throws IOException {
        var path = getPathToPatientId(patientId);
        Files.createDirectories(Path.of(path));
        return path;
    }

    private static String getPathToPatientId(Long patientId) {
        return IMAGES_PATH + PATIENT_CARD_DIRECTORY + patientId + "/";
    }
}
