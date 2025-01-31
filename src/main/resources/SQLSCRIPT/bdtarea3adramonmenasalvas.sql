-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-01-2025 a las 18:36:05
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bdtarea3adramonmenasalvas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carnets`
--

CREATE TABLE `carnets` (
  `id` bigint(20) NOT NULL,
  `distancia_recorrida` double DEFAULT NULL,
  `fecha_expedido` date DEFAULT NULL,
  `paradas_vip` int(11) DEFAULT NULL,
  `id_parada` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `carnets`
--

INSERT INTO `carnets` (`id`, `distancia_recorrida`, `fecha_expedido`, `paradas_vip`, `id_parada`) VALUES
(1, 15, '2025-01-27', 3, 1),
(2, 10, '2025-01-29', 2, 1),
(3, 5, '2025-01-29', 1, 1),
(4, 5, '2025-01-29', 0, 1),
(5, 5, '2025-01-29', 0, 2),
(6, 0, '2025-01-29', 0, 2),
(7, 0, '2025-01-29', 0, 2),
(8, 0, '2025-01-30', 0, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estancias`
--

CREATE TABLE `estancias` (
  `id` bigint(20) NOT NULL,
  `fecha` date NOT NULL,
  `vip` tinyint(1) NOT NULL DEFAULT 0,
  `id_parada` bigint(20) NOT NULL,
  `id_peregrino` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estancias`
--

INSERT INTO `estancias` (`id`, `fecha`, `vip`, `id_parada`, `id_peregrino`) VALUES
(1, '2025-01-30', 1, 1, 1),
(2, '2025-01-30', 1, 1, 2),
(3, '2025-01-30', 1, 1, 3),
(4, '2025-01-30', 0, 1, 5),
(5, '2025-01-31', 1, 1, 1),
(6, '2025-01-31', 1, 1, 1),
(7, '2025-01-31', 1, 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paradas`
--

CREATE TABLE `paradas` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `region` char(1) NOT NULL,
  `responsable` varchar(255) NOT NULL,
  `fk_usuario` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paradas`
--

INSERT INTO `paradas` (`id`, `nombre`, `region`, `responsable`, `fk_usuario`) VALUES
(1, 'gijon', 'a', 'gijon asturias', 2),
(2, 'aviles', 'a', 'aviles asturias', 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `peregrinos`
--

CREATE TABLE `peregrinos` (
  `id` bigint(20) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `nacionalidad` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `fk_usuario` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `peregrinos`
--

INSERT INTO `peregrinos` (`id`, `apellido`, `fecha_nacimiento`, `nacionalidad`, `nombre`, `fk_usuario`) VALUES
(1, 'menasalvas rodriguez', '1996-11-20', 'Suiza', 'ramon jesus', 3),
(2, 'fernandez ruiz', '1992-07-29', 'España', 'carla', 4),
(3, 'rodriguez gonzalez', '1995-01-28', 'Jamaica', 'david', 5),
(4, 'fernadez gutierrez', '1988-01-14', 'Austria', 'gabriel de la marcha', 6),
(5, 'blas', '1985-01-16', 'Brasil', 'luis de', 8),
(6, 'garcia perez', '2000-01-19', 'Bélgica', 'carlos ruben', 9),
(7, 'de la fuente', '2004-01-08', 'Bélgica', 'paco pil', 10),
(8, 'jardon rudeiros', '2024-01-15', 'Bélgica', 'isabel', 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `perfil` enum('ADMIN','INVITADO','PARADA','PEREGRINO') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `email`, `nombre`, `password`, `perfil`) VALUES
(1, 'admin@admin.com', 'admin', 'admin', 'ADMIN'),
(2, 'gijon@gijon.com', 'gijon', 'gijon', 'PARADA'),
(3, 'ramon@ramon.com', 'ramon', 'ramon', 'PEREGRINO'),
(4, 'carla@carla.es', 'carla', 'carla', 'PEREGRINO'),
(5, 'david@david.org', 'david', 'david', 'PEREGRINO'),
(6, 'gabriel@gabriel.com', 'gabriel', 'gabriel', 'PEREGRINO'),
(7, 'aviles@aviles.com', 'aviles', 'aviles', 'PARADA'),
(8, 'luis@luis.com', 'luis', 'luis', 'PEREGRINO'),
(9, 'carlos@carlos.com', 'carlos', 'carlos', 'PEREGRINO'),
(10, 'paco@paco.com', 'paco', 'paco', 'PEREGRINO'),
(11, 'isabel@isabel.com', 'isabel', 'isabel', 'PEREGRINO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `visitas`
--

CREATE TABLE `visitas` (
  `id` bigint(20) NOT NULL,
  `fecha` date NOT NULL,
  `id_parada` bigint(20) NOT NULL,
  `id_peregrino` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `visitas`
--

INSERT INTO `visitas` (`id`, `fecha`, `id_parada`, `id_peregrino`) VALUES
(1, '2025-01-27', 1, 1),
(2, '2025-01-29', 1, 2),
(3, '2025-01-29', 1, 3),
(4, '2025-01-29', 1, 4),
(5, '2025-01-29', 2, 5),
(6, '2025-01-29', 2, 6),
(7, '2025-01-29', 2, 7),
(8, '2025-01-30', 1, 1),
(9, '2025-01-30', 1, 2),
(10, '2025-01-30', 1, 3),
(11, '2025-01-30', 1, 4),
(12, '2025-01-30', 1, 5),
(13, '2025-01-30', 1, 8),
(14, '2025-01-31', 1, 1),
(16, '2025-01-31', 1, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `carnets`
--
ALTER TABLE `carnets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjppmyy6tlhqs6ogeve14cjb1s` (`id_parada`);

--
-- Indices de la tabla `estancias`
--
ALTER TABLE `estancias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKi1fam1r85nkth28g6kjlg08rf` (`id_parada`),
  ADD KEY `FKds6ch16adx2qcy06s5pax58j6` (`id_peregrino`);

--
-- Indices de la tabla `paradas`
--
ALTER TABLE `paradas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKl4ughuh9pk4fis6rlgm1vuqgm` (`fk_usuario`);

--
-- Indices de la tabla `peregrinos`
--
ALTER TABLE `peregrinos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK4uaynutokdpgh1tehwiq5p7tr` (`fk_usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`),
  ADD UNIQUE KEY `UKio49vjba68pmbgpy9vtw8vm81` (`nombre`);

--
-- Indices de la tabla `visitas`
--
ALTER TABLE `visitas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbn7gxt8joxki70da0x59p1nep` (`id_parada`),
  ADD KEY `FKchi5ajbsisud7xqwblb362804` (`id_peregrino`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `carnets`
--
ALTER TABLE `carnets`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `estancias`
--
ALTER TABLE `estancias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `paradas`
--
ALTER TABLE `paradas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `peregrinos`
--
ALTER TABLE `peregrinos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `visitas`
--
ALTER TABLE `visitas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `carnets`
--
ALTER TABLE `carnets`
  ADD CONSTRAINT `FKjppmyy6tlhqs6ogeve14cjb1s` FOREIGN KEY (`id_parada`) REFERENCES `paradas` (`id`);

--
-- Filtros para la tabla `estancias`
--
ALTER TABLE `estancias`
  ADD CONSTRAINT `FKds6ch16adx2qcy06s5pax58j6` FOREIGN KEY (`id_peregrino`) REFERENCES `peregrinos` (`id`),
  ADD CONSTRAINT `FKi1fam1r85nkth28g6kjlg08rf` FOREIGN KEY (`id_parada`) REFERENCES `paradas` (`id`);

--
-- Filtros para la tabla `paradas`
--
ALTER TABLE `paradas`
  ADD CONSTRAINT `FKh832qgfwqgobkjcr561u0cp1v` FOREIGN KEY (`fk_usuario`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `peregrinos`
--
ALTER TABLE `peregrinos`
  ADD CONSTRAINT `FKdwaj9298rn0nrhyakwopo0gsn` FOREIGN KEY (`fk_usuario`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `visitas`
--
ALTER TABLE `visitas`
  ADD CONSTRAINT `FKbn7gxt8joxki70da0x59p1nep` FOREIGN KEY (`id_parada`) REFERENCES `paradas` (`id`),
  ADD CONSTRAINT `FKchi5ajbsisud7xqwblb362804` FOREIGN KEY (`id_peregrino`) REFERENCES `peregrinos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
