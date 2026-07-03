CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comentario VARCHAR(300) NOT NULL,
    puntuacion INT NOT NULL,
    id_usuario BIGINT NOT NULL,
    id_anime BIGINT NOT NULL
);