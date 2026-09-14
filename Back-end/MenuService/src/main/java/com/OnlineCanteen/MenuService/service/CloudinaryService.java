package com.OnlineCanteen.MenuService.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /*
        Upload image to Cloudinary
    */
    public String uploadImage(MultipartFile file) throws IOException {

        validateImage(file);

        Map<?, ?> uploadResult = cloudinary.uploader().upload(

                file.getBytes(),

                ObjectUtils.asMap(

                        "folder", "online-canteen/menu"

                )

        );

        return uploadResult.get("secure_url").toString();

    }

    /*
        Delete image from Cloudinary
    */
    public void deleteImage(String imageUrl) throws IOException {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        String publicId = extractPublicId(imageUrl);

        cloudinary.uploader().destroy(

                publicId,

                ObjectUtils.emptyMap()

        );

    }

    /*
        Validate uploaded image
    */
    private void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "Please select an image."
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp"))) {

            throw new RuntimeException(

                    "Only JPG, JPEG, PNG and WEBP images are allowed."

            );
        }

        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new RuntimeException(

                    "Maximum image size is 5 MB."

            );
        }

    }

    /*
        Extract public id from Cloudinary URL
    */
    private String extractPublicId(String imageUrl) {

        String url = imageUrl.substring(

                imageUrl.indexOf("/upload/") + 8

        );

        int slashIndex = url.indexOf("/");

        if (slashIndex != -1) {

            url = url.substring(slashIndex + 1);

        }

        int dotIndex = url.lastIndexOf(".");

        return url.substring(0, dotIndex);

    }
}
