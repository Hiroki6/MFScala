package com.recommendations.collaborative_filtering.matrix_factorization.tests

import com.recommendations.collaborative_filtering.matrix_factorization.StreamRunner
import com.recommendations.collaborative_filtering.matrix_factorization.models.StreamMatrixFactorization
import com.recommendations.collaborative_filtering.matrix_factorization.preprocessings.StreamMFDGen
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global

object TestStreamMatrixFactorization extends App {

  val logger = Logger("Test Matrix Factorization")
  val userFilePath = "data/ml-100k/u.user"
  val itemFilePath = "data/ml-100k/u.item"
  val trainFilePath = "data/ml-100k/u1.base"
  val testFilePath = "data/ml-100k/u1.test"
  val smfd = new StreamMFDGen

  logger.info("initialize map")
  smfd.apply(userFilePath, itemFilePath, '|')

  logger.info("initialize")
  val mf = new StreamMatrixFactorization(smfd.userIdMap, smfd.itemIdMap, K = 100)

  logger.info("initialize runner")
  val streamRunner = new StreamRunner(smfd, mf)

  logger.info("fit")
  val start = System.currentTimeMillis
  val result = streamRunner.run(trainFilePath)
  result.onSuccess { case ioResult =>
      println(s"${ioResult.count}処理しました")
  }
  println((System.currentTimeMillis - start)/1000)
}