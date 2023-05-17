# GameDP-SC-Code (Code for "Dynamic Private Task Assignment under Differential Privacy" published in ICDE 2023)
## Data set generation (only for synthetic data sets)
Run: `edu.ecnu.dll.dataset.run.GenerateBasicUniformDataset.main($UniformOutputPath)` to generate uniform data sets;
Run: `edu.ecnu.dll.dataset.run.GenerateBasicNormalDataset.main($NormalOutputPath)` to generate normal data sets.
 
## Data set preprocess
Run: `edu.ecnu.dll.dataset.run.GenerateBatch.main($DatasetPath)` to generate batched data sets for each kind of data set types (chengdu, normal and uniform)

## Batch Run (results are recorded in $BasicOutputPath/)
Run the command:
`nohup bash script/gen_run_batch_total.sh $` 5 times for each data set to get the results (5 results for each kind of data sets).
(Notice that you need set the different parameters in the script for different kinds of data sets).


## Average combine
Run `edu.ecnu.dll.dataset.batch.concat_data.AverageTotalExperimentData.averageCSVFile($BasicOutputPath, 5)` to get the final experiment results.

