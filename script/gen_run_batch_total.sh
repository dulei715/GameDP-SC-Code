#!/bin/bash

# Define the basic variables
batch_array_size=260	# 260 for chengdu, 300 for normal and uniform
scale_distance=0.01		# 0.01 for chengdu, 1 for normal and uniform

# set the basic_path as the path which contains batch_dataset (split by preprocess).
# "{$SelfDefinedPath}/chengdu" for chengdu, "{$SelfDefinedPath}/normal", and "{$SelfDefinedPath}/uniform" for uniform
basic_path="{$SelfDefinedPath}/3_chengdu"

# set result name title. ChengduDidi for chengdu; Normal for normal; Uniform for uniform
dataset_result_title=ChengduDidi 

# generate basic dataset data (Only containing the locations of tasks and workers)
java -cp GTDP.jar edu.ecnu.dll.dataset.run.GenerateBatch "${basic_path}"


# Generate and record the privacy budget; calculate relative real distances for each task-worker pair; generate and record noise distance for each task-worker pair
java -cp GTDP.jar edu.ecnu.dll.dataset.run.GenerateWorkerScaleDataset "${basic_path}" false true "${scale_distance}" 0


# Run experiment to output and record the results (except the budget-change result)
java -cp GTDP.jar edu.ecnu.dll.run.batch_run_total.batch_combine.TaskValueWorkerRangeAndWorkerRatioBatchRun "${basic_path}" 0 "${dataset_result_title}" "${batch_array_size}"

# Delete all unecessary data
rm -rf ${basic_path}/task_worker_1_1_*
rm -rf ${basic_path}/task_worker_1_3_*
rm -rf ${basic_path}/task_worker_1_2_5
rm -rf ${basic_path}/task_worker_1_2_0/*budget.txt
rm -rf ${basic_path}/task_worker_1_2_0/*distance.txt

# generate and record the fixed range privacy budget; generate and record noise distance for each task-worker pair under given different privacy budget range
java -cp GTDP.jar edu.ecnu.dll.dataset.run.GeneratePrivacyScaleDataset "${basic_path}/task_worker_1_2_0" false true

# Run experiment to output and record the results for budget-change result
java -cp GTDP.jar edu.ecnu.dll.run.batch_run_total.batch_combine.PrivacyBudgetBatchRun "${basic_path}" 0 "${dataset_result_title}" "${batch_array_size}"

# Combine all batches's experiemnt
java -cp GTDP.jar edu.ecnu.dll.dataset.batch.concat_data.ConcatExperimentData "${basic_path}" "${batch_array_size}"