import { useEffect, useState } from 'react'
import { fetchSummary, fetchLeaderboard } from './api/client'

interface Summary {
  totalPRs: number
  totalBuilds: number
  successBuilds: number
  failedBuilds: number
  buildSuccessRate: number
  totalDevelopers: number
}

interface Developer {
  githubLogin: string
  displayName: string
  avatarUrl: string
  totalPRs: number
}

function App() {
  const [summary, setSummary] = useState<Summary | null>(null)
  const [leaderboard, setLeaderboard] = useState<Developer[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([fetchSummary(), fetchLeaderboard()])
      .then(([summaryData, leaderboardData]) => {
        setSummary(summaryData)
        setLeaderboard(leaderboardData)
        setLoading(false)
      })
  }, [])

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-950 flex items-center justify-center">
        <p className="text-white text-xl">Loading DevFlow...</p>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white p-8">

      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-white">DevFlow Dashboard</h1>
        <p className="text-gray-400 mt-1">Developer Workflow Intelligence Platform</p>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <div className="bg-gray-900 rounded-xl p-6 border border-gray-800">
          <p className="text-gray-400 text-sm">Total PRs</p>
          <p className="text-3xl font-bold text-white mt-1">{summary?.totalPRs}</p>
        </div>
        <div className="bg-gray-900 rounded-xl p-6 border border-gray-800">
          <p className="text-gray-400 text-sm">Total Builds</p>
          <p className="text-3xl font-bold text-white mt-1">{summary?.totalBuilds}</p>
        </div>
        <div className="bg-gray-900 rounded-xl p-6 border border-gray-800">
          <p className="text-gray-400 text-sm">Build Success Rate</p>
          <p className="text-3xl font-bold text-green-400 mt-1">{summary?.buildSuccessRate}%</p>
        </div>
        <div className="bg-gray-900 rounded-xl p-6 border border-gray-800">
          <p className="text-gray-400 text-sm">Active Developers</p>
          <p className="text-3xl font-bold text-white mt-1">{leaderboard.length}</p>
        </div>
      </div>

      {/* Leaderboard */}
      <div className="bg-gray-900 rounded-xl border border-gray-800">
        <div className="p-6 border-b border-gray-800">
          <h2 className="text-xl font-semibold">Developer Leaderboard</h2>
          <p className="text-gray-400 text-sm mt-1">Ranked by pull request activity</p>
        </div>
        <div className="divide-y divide-gray-800">
          {leaderboard.map((dev, index) => (
            <div key={dev.githubLogin} className="flex items-center gap-4 p-4 hover:bg-gray-800 transition">
              <span className="text-gray-500 w-6 text-sm font-mono">{index + 1}</span>
              <img
                src={dev.avatarUrl}
                alt={dev.githubLogin}
                className="w-9 h-9 rounded-full"
              />
              <div className="flex-1">
                <p className="font-medium text-white">{dev.githubLogin}</p>
              </div>
              <div className="text-right">
                <p className="text-white font-semibold">{dev.totalPRs}</p>
                <p className="text-gray-500 text-xs">PRs</p>
              </div>
            </div>
          ))}
        </div>
      </div>

    </div>
  )
}

export default App