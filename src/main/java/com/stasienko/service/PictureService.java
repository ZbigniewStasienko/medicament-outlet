package com.stasienko.service;

import com.stasienko.model.Picture;
import com.stasienko.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    public Picture getPictureById(UUID id) {
        return pictureRepository.findById(id).orElse(null);
    }

    public Picture addPicture(MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            Picture picture = new Picture();
            picture.setData(multipartFile.getBytes());
            return pictureRepository.save(picture);
        }
        return null;
    }

    public Picture updatePicture(UUID id, Picture updatedPicture) {
        return pictureRepository.findById(id)
                .map(picture -> {
                    picture.setData(updatedPicture.getData());
                    return pictureRepository.save(picture);
                }).orElseGet(() -> {
                    updatedPicture.setId(id);
                    return pictureRepository.save(updatedPicture);
                });
    }

    public void deletePicture(UUID id) {
        pictureRepository.deleteById(id);
    }
}
