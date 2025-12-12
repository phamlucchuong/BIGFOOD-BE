package com.example.bigfood.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Tải tệp lên Cloudinary và trả về URL an toàn
     * 
     * @param file Tệp MultipartFile từ Controller
     * @return URL an toàn (HTTPS) của tệp đã tải lên
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {

        // Cấu hình các tham số tải lên (Tùy chọn)
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", "auto"); // Tự động nhận diện loại tệp (image, video, raw)

        if (folder != null && !folder.isEmpty())
            params.put("folder", "bigfood/" + folder); // Đặt trong một thư mục cụ thể

        // Tải tệp lên
        // Sử dụng file.getBytes() để truyền dữ liệu byte
        Map result = cloudinary.uploader().upload(file.getBytes(), params);

        // Lấy URL an toàn (HTTPS) từ kết quả
        // String secureUrl = (String) result.get("secure_url");

        // Trong trường hợp Production, bạn nên lưu lại public_id thay vì toàn bộ URL
        String publicId = (String) result.get("public_id");

        return publicId;
    }

    /**
     * Xóa tệp từ Cloudinary (Sử dụng Public ID)
     * 
     * @param publicId ID công khai của tệp (Ví dụ:
     *                 "my_springboot_project/167888888")
     */
    public void deleteFile(String publicId) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", "image"); // Phải chỉ định loại tài nguyên

        cloudinary.uploader().destroy(publicId, params);
    }


    @Named("generateUrl")
    public String generateUrl(String publicId) {
        return cloudinary.url()
                .secure(true)
                .generate(publicId);
    }
}
