package ru.robilko.league_details.domain.repo

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import ru.robilko.base.util.Response
import ru.robilko.league_details.LeagueDetailsMock
import ru.robilko.league_details.data.remote.LeagueDetailsRemoteDataSource
import ru.robilko.league_details.data.repo.LeagueDetailsRepositoryImpl
import ru.robilko.remote.data.model.asDomainModel

class LeagueDetailsRepositoryTest {
    private val remoteDataSource = mockk<LeagueDetailsRemoteDataSource>()
    private val repo = LeagueDetailsRepositoryImpl(remoteDataSource)

    @Test
    fun `get success league details test`() {
        val leagueId = LeagueDetailsMock.LEAGUE_ID
        val leagueDto = LeagueDetailsMock.leagueDto
        every { runBlocking { remoteDataSource.getLeague(leagueId) } } returns leagueDto
        val actual = runBlocking { repo.getLeague(leagueId) }
        val league = leagueDto.asDomainModel()
        val expected = Response.Success(league)
        assertEquals(expected, actual)
        verify(exactly = 1) { runBlocking { remoteDataSource.getLeague(leagueId) } }
    }

    @Test
    fun `get failure league details test`() {
        val leagueId = LeagueDetailsMock.LEAGUE_ID
        val expectedErrorCode = 404
        val expectedException = HttpException(
            retrofit2.Response.error<Any>(
                expectedErrorCode,
                "".toResponseBody(null)
            )
        )
        every { runBlocking { remoteDataSource.getLeague(leagueId) } } throws expectedException
        val actual = runBlocking { repo.getLeague(leagueId) }
        val expected = Response.Failure(false, expectedErrorCode, "")
        assertEquals(expected, actual)
        verify(exactly = 1) { runBlocking { remoteDataSource.getLeague(leagueId) } }
    }
}