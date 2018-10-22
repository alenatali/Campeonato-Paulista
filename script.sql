CREATE DATABASE av1
GO
USE av1

CREATE TABLE time_ (
codigo  INT IDENTITY,
nome    VARCHAR(50) NOT NULL,
cidade  VARCHAR(80) NOT NULL,
estadio VARCHAR(60),
ordem INT,
grupo INT

PRIMARY KEY (codigo)
)

CREATE TABLE jogo (
time_a_codigo INT,
time_b_codigo INT,
gols_time_a   TINYINT NOT NULL,
gols_time_b	  TINYINT NOT NULL,
fase          TINYINT NOT NULL CHECK(fase > -1 AND fase < 4), -- 0 = prim. fase, 1 = quartas, 2 = semi, 3 = final
data_         DATETIME

PRIMARY KEY(time_a_codigo, time_b_codigo, data_)
FOREIGN KEY (time_a_codigo) REFERENCES time_(codigo),
FOREIGN KEY (time_b_codigo) REFERENCES time_(codigo)
)

--
-- definição das views
CREATE VIEW grupo_a AS
SELECT ordem, codigo, nome AS 'time', 'A' AS grupo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 1
GO
CREATE VIEW grupo_b AS
SELECT ordem, codigo, nome AS 'time', 'B' AS grupo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 2
GO
CREATE VIEW grupo_c AS
SELECT ordem, codigo, nome AS 'time', 'C' AS grupo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 3
GO
CREATE VIEW grupo_d AS
SELECT ordem, codigo, nome AS 'time', 'D' AS grupo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 4
GO
CREATE VIEW grupos AS
SELECT * FROM grupo_a
UNION
SELECT * FROM grupo_b
UNION
SELECT * FROM grupo_c
UNION
SELECT * FROM grupo_d

CREATE VIEW v_random
AS
SELECT RAND() AS random

--
-- Inserção times
INSERT INTO time_ (nome, cidade, estadio) VALUES
('São Paulo', 'São Paulo', 'Morumbi'),
('Corinthians', 'São Paulo', 'Arena Corinthians'),
('Palmeiras', 'São Paulo', 'Allianz Parque'),
('Santos', 'Santos', 'Vila Belmiro'),
('Água Santa', 'Diadema', 'Distrital do Inamar'),
('Audax', 'Osasco', 'José Liberatti'),
('Botafogo', 'Ribeirão Preto', 'Santa Cruz'),
('Capivariano', 'Capivari', 'Arena Capivari'),
('Ferroviária', 'Araraquara', 'Fonte Luminosa'),
('Ituano', 'Itu', 'Novelli Júnior'),
('Mogi Mirim', 'Mogi Mirim', 'Vali Chaves'),
('Linense', 'Lins', 'Gilberto Siqueira Lopes'),
('Novorizontino', 'Novo Horizonte', 'Jorge Ismael de Biasi'),
('Oeste', 'Itápolis', 'Amaros'),
('Ponte Preta', 'Campinas', 'Moisés Lucarelli'),
('Red Bull Brasil', 'Campinas', 'Moisés Lucarelli'),
('Rio Claro', 'Rio Claro', 'Augusto Schmidt Filho'),
('São Bento', 'Sorocaba', 'Walter Ribeiro'),
('São Bernardo', 'São Bernado do Campo', 'Primeiro de Maio'),
('XV', 'Piracicaba', 'Barão de Serra Negra')

CREATE PROCEDURE sp_chaveador 
AS
	DECLARE @count INT,
		    @grupo INT,
			@group CHAR(1)
	SET @count = 1
	SET @grupo = 1

	-- gera aleatoriedade na coluna ordem
	WHILE (@count < 21) 
	BEGIN
		UPDATE time_ SET ordem = abs(checksum(NewId()) % 10000) WHERE codigo = @count
		SET @count += 1
	END

	-- redefine os grupos para 0
	UPDATE time_ SET grupo = 0

	-- define os times cabeça de cada chave
	UPDATE time_ SET grupo = codigo
	WHERE codigo <= 4

	-- separa times com base na aleatoriedade gerada
	SET @grupo = 1
	WHILE (@grupo < 5)
	BEGIN
		UPDATE time_ SET grupo = @grupo
			WHERE codigo IN (	
				SELECT TOP(4) codigo FROM time_
				WHERE grupo = 0 AND
				nome != 'São Paulo' AND
				nome != 'Corinthians' AND
				nome != 'Palmeiras' AND
				nome != 'Santos'
				ORDER BY ordem ASC
				)
		SET @grupo = @grupo + 1
	END
------------

-- retorna aleatóriamente um dos seguites horarios: 16:30, 19:00 ou 22:15
CREATE FUNCTION fn_random_schedule()
RETURNS VARCHAR(12)
AS
BEGIN
	DECLARE @rand_hour INT,
			@hour VARCHAR(12)
	SET @rand_hour = FLOOR((SELECT TOP(1) random FROM v_random)*3+1)
	SET @hour = '16:30:00.000'

	IF (@rand_hour = 2)
	BEGIN
		RETURN '19:00:00.000'
	END
	ELSE
	BEGIN
		IF (@rand_hour > 2)
		BEGIN
			RETURN '22:15:00.000'
		END
	END
	RETURN @hour
END
-----------------------------------------------

-- Avança a data até aproxima quarta ou domigo
CREATE FUNCTION fn_next_date (@date DATETIME)
RETURNS DATETIME
AS
BEGIN
	WHILE(0 = 0)
	BEGIN
		SET @date = DATEADD(DAY, 1, @date)
		IF (DATEPART( WEEKDAY, @date ) = 1 OR
			DATEPART( WEEKDAY, @date ) = 4)
		BEGIN
			BREAK
		END
	END
	SET @date = SUBSTRING(CAST(@date AS VARCHAR(24)), 0, 12) + ' ' + dbo.fn_random_schedule()
	RETURN @date
END
-----------------------------------------------

-- procura uma data que, nem o time A nem o time B, não tenham jogos agendados
DROP FUNCTION fn_available_agenda
CREATE FUNCTION fn_available_agenda (@cod_t_a INT, @cod_t_b INT)
RETURNS DATETIME
AS
BEGIN
	DECLARE @date DATETIME,
			@date_c VARCHAR(10),
			@a    INT,
			@b    INT
	SET @date = '2016-30-01 00:00:00.000'
			WHILE(0 = 0)
			BEGIN
				SET @date = dbo.fn_next_date(@date)
				SET @date_c = SUBSTRING(CAST(@date AS VARCHAR(23)), 1 , 10)
				SET @a = (SELECT COUNT(*) from jogo 
						  WHERE 
								(time_a_codigo = @cod_t_a OR time_b_codigo = @cod_t_a)
								AND SUBSTRING(CAST(data_ AS VARCHAR(23)), 1 , 10) = @date_c)
				SET @b = (SELECT COUNT(*) from jogo 
						  WHERE 
								(time_a_codigo = @cod_t_b OR time_b_codigo = @cod_t_b)
								AND SUBSTRING(CAST(data_ AS VARCHAR(23)), 1 , 10) = @date_c)
				IF (@a < 1 AND @b < 1)
				BEGIN
					BREAK
				END
			END
	RETURN @date
END
/*
SELECT COUNT(*) from jogo WHERE time_a_codigo = @cod_t_a AND data_ = @date
select * from jogo
select TOP(1) * from jogo
print(dbo.fn_available_agenda(1, 2)) --Jan 31 2016 10:15PM
print(dbo.fn_available_agenda(1, 3)) --Fev  3 2016  7:00PM
print(dbo.fn_available_agenda(2, 3)) --Jan 31 2016 10:15PM
update jogo set data_ = dbo.fn_next_date('2016-30-01 00:00:00.000') where time_a_codigo = 1 AND time_b_codigo = 2
update jogo set data_ = dbo.fn_next_date('2016-03-02 00:00:00.000') where time_a_codigo = 1 AND time_b_codigo = 3
update jogo set data_ = dbo.fn_next_date('2016-03-02 00:00:00.000') where time_a_codigo = 2 AND time_b_codigo = 3
update jogo set data_ = dbo.fn_next_date('2016-03-02 00:00:00.000') where time_a_codigo = 2 AND time_b_codigo = 3
*/
-----------------------------------------------

-- Organiza partidas com horário aleatórios, através da ordem e divisão de grupos
DROP PROCEDURE sp_gerador_jogos_oitavas
GO
CREATE PROCEDURE sp_gerador_jogos_oitavas
AS
	TRUNCATE TABLE jogo
	DECLARE @query    VARCHAR(MAX),
			@c_rodadas INT
	SET @c_rodadas = 1

	WHILE (@c_rodadas < 4)
	BEGIN
		DECLARE @g11 CHAR,
				@g12 CHAR,
				@g21 CHAR,
				@g22 CHAR

		IF (@c_rodadas = 1)
		BEGIN
			SET @g11 = 'a'
			SET @g12 = 'b'
			SET @g21 = 'c'
			SET @g22 = 'd'
		END
		ELSE
		BEGIN
			IF(@c_rodadas = 2)
			BEGIN
				SET @g11 = 'a'
				SET @g12 = 'c'
				SET @g21 = 'b'
				SET @g22 = 'd'
			END
			ELSE
			BEGIN
				SET @g11 = 'a'
				SET @g12 = 'd'
				SET @g21 = 'b'
				SET @g22 = 'c'
			END
		END

		SET @query = '
			DECLARE @oitavas  TABLE (
			codigo_a  INT,
			codigo_b  INT,
			data_     DATETIME
		)
		INSERT INTO @oitavas (codigo_a, codigo_b)
		SELECT '+@g11+'.codigo, '+@g12+'.codigo FROM grupo_'+@g11+' '+@g11+'
		CROSS JOIN grupo_'+@g12+' '+@g12+'

		INSERT INTO jogo (time_a_codigo, time_b_codigo, data_, gols_time_a, gols_time_b, fase)
		SELECT codigo_a, codigo_b, ''2000-01-01 00:00:00.000'', 0, 0, 0 FROM @Oitavas '

		EXEC(@query)

		SET @query = '
			DECLARE @oitavas  TABLE (
			codigo_a  INT,
			codigo_b  INT,
			data_     DATETIME
		)
		INSERT INTO @oitavas (codigo_a, codigo_b)
		SELECT '+@g21+'.codigo, '+@g22+'.codigo FROM grupo_'+@g21+' '+@g21+'
		CROSS JOIN grupo_'+@g22+' '+@g22+'

		INSERT INTO jogo (time_a_codigo, time_b_codigo, data_, gols_time_a, gols_time_b, fase)
		SELECT codigo_a, codigo_b, ''2000-01-01 00:00:00.000'', 0, 0, 0 FROM @Oitavas '

		EXEC(@query)
		
		SET @c_rodadas += 1
	END

	-- atribuição de datas
	DECLARE @cod_t_a  INT,
			@cod_t_b  INT

	-- executa enquanto ouver datas default
	WHILE ((select top(1) COUNT(*) from jogo where data_ = '2000-01-01 00:00:00.000') > 0)
	BEGIN
		SET @cod_t_a = (select top(1) time_a_codigo from jogo where data_ = '2000-01-01 00:00:00.000')
		SET @cod_t_b = (select top(1) time_b_codigo from jogo where data_ = '2000-01-01 00:00:00.000')
		UPDATE TOP(1) jogo SET data_ = dbo.fn_available_agenda(@cod_t_a, @cod_t_b) where data_ = '2000-01-01 00:00:00.000'
	END
-----------------------------------------------

/*
-- Gerar aleatórimente o placar dos jogos da fase informada, zerando os placares posteriores
CREATE PROCEDURE sp_gerador_placar (@fase INT OUTPUT)
AS
	UPDATE jogo SET gols_time_a = -1, gols_time_b = -1 WHERE fase >= @fase

	DECLARE @qtd_gols_a INT,
			@qtd_gols_b INT

	WHILE ((SELECT TOP(1) COUNT(*) FROM jogo WHERE gols_time_a + gols_time_b < 0) > 0)
	BEGIN
		SET @qtd_gols_a = ABS(CHECKSUM(NEWID()) % 10)
		SET @qtd_gols_b = ABS(CHECKSUM(NEWID()) % 10)

		UPDATE TOP(1) jogo SET gols_time_a = @qtd_gols_a, gols_time_b = @qtd_gols_b
		WHERE fase = @fase
	END
-----------------------------------------------
*/