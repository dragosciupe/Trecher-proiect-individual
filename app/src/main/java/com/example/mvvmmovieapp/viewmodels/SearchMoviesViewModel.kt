package com.example.mvvmmovieapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmmovieapp.apidata.search.SearchResponse
import com.example.mvvmmovieapp.repositories.MovieApiRepository
import com.example.mvvmmovieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchMoviesViewModel @Inject constructor(private val repository: MovieApiRepository) : ViewModel() {
    var movieSearchResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    fun searchMovie(movieName: String) = viewModelScope.launch {
        movieSearchResponse.postValue(Resource.Loading())
        val response = repository.searchMovie(movieName)
        movieSearchResponse.postValue(handleMovieSearchResponse(response))
    }

    private fun handleMovieSearchResponse(
        response: Response<SearchResponse>
    ): Resource<SearchResponse> {
        if(response.isSuccessful) {
            response.body()?.let { searchResponse ->
                return Resource.Success(searchResponse)
            }
        }
        return Resource.Error(response.message())
    }
}