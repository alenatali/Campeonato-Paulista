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
gols_time_a   TINYINT NOT NULL CHECK (gols_time_a > -1),
gols_time_b	  TINYINT NOT NULL CHECK (gols_time_b > -1),
data_         DATETIME

PRIMARY KEY(time_a_codigo, time_b_codigo, data_)
FOREIGN KEY (time_a_codigo) REFERENCES time_(codigo),
FOREIGN KEY (time_b_codigo) REFERENCES time_(codigo)
)

CREATE VIEW grupo_a AS
SELECT ordem,  nome AS 'time', 'A' AS grupo, codigo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 1

CREATE VIEW grupo_b AS
SELECT ordem,  nome AS 'time', 'B' AS grupo, codigo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 2

CREATE VIEW grupo_c AS
SELECT ordem,  nome AS 'time', 'C' AS grupo, codigo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 3

CREATE VIEW grupo_d AS
SELECT ordem,  nome AS 'time', 'D' AS grupo, codigo, ROW_NUMBER() OVER(ORDER BY ordem) AS 'row_order' FROM time_
WHERE grupo = 4

CREATE VIEW grupos AS
SELECT * FROM grupo_a
UNION
SELECT * FROM grupo_b
UNION
SELECT * FROM grupo_c
UNION
SELECT * FROM grupo_d

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
('Novorizintino', 'Novo Horizonte', 'Jorge Ismael de Biasi'),
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
CREATE PROCEDURE sp_random_schedule	(@hour VARCHAR(5) OUTPUT)
AS
	DECLARE @rand_hour INT
	SET @rand_hour = ABS(CHECKSUM(NEWID()) % 3) + 1
	SET @hour = '16:30:00.000'
	IF (@rand_hour = 2)
	BEGIN
		SET @hour = '19:00:00.000'
	END
	ELSE
	BEGIN
		IF (@rand_hour > 2)
		BEGIN
			SET @hour = '22:15:00.000'
		END
	END
-----------------------------------------------

-- Avança a data até aproxima quarta ou domigo
CREATE PROCEDURE sp_sun_or_wed (@date DATETIME OUTPUT)
AS
	DECLARE @day_bool BIT
	SET @day_bool = 0
	WHILE(@day_bool = 0)
	BEGIN
		SET @date = DATEADD(DAY, 1, @date)
		IF (
			DATEPART( WEEKDAY, @date ) = 1 OR
			DATEPART( WEEKDAY, @date ) = 4
		)
		BEGIN
			SET @day_bool = 1
		END
	END
-----------------------------------------------
	

CREATE PROCEDURE sp_datas_oitavas
	DECLARE @oitavas  TABLE (
			row_order INT,
			codigo_a  INT,
			codigo_b  INT,
			data_     DATETIME
		)
	
	-- laco que execute o INSERT do CROSS JOIN entre
	-- ab, cd, ac, bd, ad e bc

	-- codigo exemplo:  grupo a e grupo b
	INSERT INTO @oitavas (codigo_a, codigo_b)
	SELECT a.codigo, b.codigo FROM grupo_a a
	CROSS JOIN grupo_b b

	-- tentativa de numerar as linhas
	--INSERT INTO @oitavas (row_order) 
	--SELECT DISTINCT ROW_NUMBER() OVER(ORDER BY codigo_b) AS 'row_order' FROM @oitavas

	--
	-- adicionar datas, restringindo times de jogarem mais de um vez no mesmo dia

	SELECT * FROM @oitavas
	
	
	
	
	
	
	
	
	
	


	-- ERRR...

alter PROCEDURE sp_gerar_datas
AS
	DECLARE @is_empty BIT
	SET @is_empty = 0
	DECLARE @stack TABLE (
		codigo  INT,
		nome    VARCHAR(50) NOT NULL,
		cidade  VARCHAR(80) NOT NULL,
		estadio VARCHAR(60),
		ordem INT,
		grupo INT
	)
	INSERT INTO @stack SELECT * FROM time_
	TRUNCATE TABLE jogo

	-- mecanismo de pilha
	WHILE (@is_empty = 0)
	BEGIN
		DECLARE @hour VARCHAR(5),
				@team_count INT,
				@date DATETIME
		SET @team_count = 1
		SET @date = '2016-30-01 00:00:00.000' -- ANO DIA MES

		--get date
		WHILE (@team_count < 21)
		BEGIN
			EXEC sp_random_schedule @hour OUTPUT

			DECLARE @top_stack_codigo INT,
					@top_stack_grupo INT,
					@count_grupo INT
			SET @top_stack_codigo = (SELECT TOP(1) codigo FROM @stack)
			SET @top_stack_grupo = (SELECT TOP(1) grupo FROM @stack)
			SET @count_grupo = (SELECT grupo FROM @stack WHERE codigo = @team_count)

			-- se o time do topo da stack não for o mesmo do contador ou do mesmo grupo do contador
			IF ( @top_stack_codigo != @team_count ) 
				AND 
			   ( @top_stack_grupo != @count_grupo )
			BEGIN
				EXEC sp_sun_or_wed @date OUTPUT
				INSERT INTO jogo (time_a_codigo, time_b_codigo, gols_time_a, gols_time_b, data_) VALUES
				(@team_count, -- todos os times
				 (SELECT TOP(1) codigo FROM @stack), -- stack de times
				 0,
				 0,
				 @date)
			 END

			-- gerar partidas
			SET @team_count += 1
		END

		-- controle de do laço
		DELETE FROM @stack WHERE codigo = (SELECT TOP(1) codigo FROM @stack)
		IF NOT EXISTS (SELECT 1 FROM @stack)
		BEGIN
			SET @is_empty = 1
		END
	END


exec sp_gerar_datas
SELECT * FROM jogo


-- Adicionar hora numa data
DECLARE @date DATETIME
SET @date = '2016-30-01 00:00:00.000'
SET @date = SUBSTRING (CAST(@date AS VARCHAR(23)), 0, 12 ) + ' ' + '01:00:00.000'
PRINT(@date)


-- Criar o sistema que organiza os jogos em horarios pré definidos de forma aleatória
-- iniciando por 30 de janeiro de 2016 e saltando até o proximo dia de jogo e assim sucessivamente
-- até todos os times enfrentarem os outros times que não do mesmo grupo


-- inicio 30 janeiro 2016
-- 10 jogos na quarta 10 jogos no domingo
-- 16:30  19:00  22:15
-- os 2 melhores de cada chave avançam as quartas de final