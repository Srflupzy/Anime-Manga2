CREATE TABLE animes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    estudio VARCHAR(100) NOT NULL,
    temporadas INT NOT NULL,
    id_genero BIGINT NOT NULL
);