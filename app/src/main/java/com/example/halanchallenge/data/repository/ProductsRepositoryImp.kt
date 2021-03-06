package com.example.halanchallenge.data.repository

import com.example.halanchallenge.data.source.remote.HalanService
import com.example.halanchallenge.domain.repository.remote.ProductsRepository
import com.example.halanchallenge.domain.repository.remote.models.Product
import com.example.halanchallenge.domain.repository.remote.models.WrappedListResponse
import com.example.halanchallenge.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepositoryImp @Inject constructor(
    private val productService: HalanService.ProductsService
) : ProductsRepository {
    override suspend fun products(token: String): Flow<Result<WrappedListResponse<Product>, String>> {
        return flow {
            val body = if (token.isEmpty()) {
                ""
            } else {
                "Bearer $token"
            }
            val api = productService.getProducts(body)
            if (api.isSuccessful) {

                val response = api.body() as WrappedListResponse<Product>

                if (response.status == "OK" && !response.data.isNullOrEmpty()) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } else {
                when {
                    api.code() == 400 -> {
                        // Bad Request
                        emit(Result.Error("Bad Request"))
                    }
                    api.code() == 401 -> {
                        emit(Result.Error("UNAUTHORIZED: Token expired"))
                    }
                    else -> {
                        emit(Result.Error(api.message()))
                    }
                }
            }
        }
    }
}