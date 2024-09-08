package ru.robilko.league_details.data.remote

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import ru.robilko.league_details.LeagueDetailsMock
import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.DefaultResponseDto

class LeagueDetailsRemoteDataSourceTest {
    private val api = mockk<BasketballApi>()
    private val dataSource = LeagueDetailsRemoteDataSourceImpl(api)
    private val leagueId = LeagueDetailsMock.LEAGUE_ID

    @Test
    fun `get league details throw exception test`() {
        every { runBlocking { api.getLeagues(id = leagueId) } } throws Exception()
        assertThrows(Exception::class.java) { runBlocking { dataSource.getLeague(leagueId) } }
        verify(exactly = 1) { runBlocking { api.getLeagues(id = leagueId) } }
    }

    @Test
    fun `get success league details test`() {
        every { runBlocking { api.getLeagues(id = leagueId) } } returns
                DefaultResponseDto(1, listOf(LeagueDetailsMock.leagueDto))

        val actual = runBlocking { dataSource.getLeague(leagueId) }
        val expected = LeagueDetailsMock.leagueDto
        assertEquals(expected, actual)
        verify(exactly = 1) { runBlocking { api.getLeagues(id = leagueId) } }
    }
}