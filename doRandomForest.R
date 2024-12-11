library(tidyverse)
library(caret)
library(randomForest)

##read the table
file_name <- "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\parsedPatientData.csv"
data <- read.table(file_name,header=TRUE,sep="\t")

#data preprossesing
#data$vitalStatus_binary <- ifelse(data$vitalStatus == "Alive",1,0)

unique_bacteria <- unique(unlist(strsplit(paste(data$bactNames,collapse = ","),",")))
binary_matrix <- sapply(unique_bacteria, function(bact) {
  sapply(data$bactNames, function(row) {
    if (length(grep(bact,row)) > 0) 1 else 0
  })
})
binary_data <-cbind(data %>% select(-bactNames), as.data.frame(binary_matrix))
binary_data$vitalStatus <- ifelse(binary_data$vitalStatus == "Alive",1,0)

#train-test split
set.seed(123)
train_index <- createDataPartition(binary_data$vitalStatus, p =0.8, list = FALSE)
train_data <- binary_data[train_index, ]
test_data <- binary_data[-train_index, ]

#separate features and target
train_features <- train_data %>% select(-sampleID, -vitalStatus)
train_features[is.na(train_features)] <- 0
train_target <- train_data$vitalStatus

test_features <- test_data %>% select(-sampleID, -vitalStatus)
test_target <- test_data$vitalStatus

#train randomForest model
rf_model <- randomForest(x = train_features, y = as.factor(train_target), ntree = 100)
#error but im going to type more

predictions <- predict(rf_model, test_features)

confusion_matrix <- confusionMatrix(predictions, as.factor(test_target))
print(confusion_matrix)

varImpPlot(rf_model)
