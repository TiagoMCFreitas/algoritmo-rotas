# Estágio de construção
FROM maven:3.8.5-openjdk-17 as build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Adiciona todos os arquivos do diretório atual para o diretório /app no contêiner
ADD . /app

# Baixa e resolve todas as dependências do Maven
RUN mvn dependency:resolve

# Compila o código fonte e empacota o aplicativo
RUN mvn package

# Estágio de execução
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR gerado do estágio de construção para o diretório de trabalho
COPY --from=build /app/target/*.jar app.jar

# Comando para executar o aplicativo quando o contêiner for iniciado
CMD ["java", "-jar", "app.jar"]
