CREATE TABLE episodios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    duracion INT NOT NULL,
    id_temporada BIGINT NOT NULL
);