import { useEffect, useState } from 'react'
import { fetchSummary, fetchLeaderboard, fetchAllPRs } from './api/client'
import StatCard from './components/StatCard'
import PRTable from './components/PRTable'
import PRChart from './components/PRChart'

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

interface PR {
  id: string
  title: string
  state: string
  createdAt: string
  mergedAt: string | null
  author: {
    githubLogin: string
    avatarUrl: string
  }
}

type Tab = 'dashboard' | 'prs' | 'leaderboard'

function App() {
  const [summary, setSummary] = useState<Summary | null>(null)
  const [leaderboard, setLeaderboard] = useState<Developer[]>([])
  const [prs, setPRs] = useState<PR[]>([])
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState<Tab>('dashboard')

  useEffect(() => {
    Promise.all([fetchSummary(), fetchLeaderboard(), fetchAllPRs()])
      .then(([summaryData, leaderboardData, prData]) => {
        setSummary(summaryData)
        setLeaderboard(leaderboardData)
        setPRs(prData)
        setLoading(false)
      })
  }, [])

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-950 flex items-center justify-center">
        <div className="text-center">
          <div className="w-8 h-8 border-2 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-400">Loading DevFlow...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white">

      {/* Sidebar */}
      <div className="fixed left-0 top-0 h-full w-56 bg-gray-900 border-r border-gray-800 p-4">
        <div className="mb-8">
          <h1 className="text-lg font-bold text-white">DevFlow</h1>
          <p className="text-gray-500 text-xs">Intelligence Platform</p>
        </div>
        <nav className="space-y-1">
          {[
            { id: 'dashboard', label: '📊 Dashboard' },
            { id: 'prs', label: '🔀 Pull Requests' },
            { id: 'leaderboard', label: '🏆 Leaderboard' },
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id as Tab)}
              className={`w-full text-left px-3 py-2 rounded-lg text-sm transition ${
                activeTab === tab.id
                  ? 'bg-blue-600 text-white'
                  : 'text-gray-400 hover:bg-gray-800 hover:text-white'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </div>

      {/* Main Content */}
      <div className="ml-56 p-8">

        {/* Dashboard Tab */}
        {activeTab === 'dashboard' && (
          <div>
            <div className="mb-6">
              <h2 className="text-2xl font-bold">Dashboard</h2>
              <p className="text-gray-400 text-sm mt-1">Overview of your engineering team</p>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
              <StatCard title="Total PRs" value={summary?.totalPRs ?? 0} />
              <StatCard title="Total Builds" value={summary?.totalBuilds ?? 0} />
              <StatCard
                title="Build Success Rate"
                value={`${summary?.buildSuccessRate ?? 0}%`}
                color="green"
              />
              <StatCard
                title="Active Developers"
                value={leaderboard.length}
                color="blue"
              />
            </div>
            <PRChart prs={prs} />
          </div>
        )}

        {/* PRs Tab */}
        {activeTab === 'prs' && (
          <div>
            <div className="mb-6">
              <h2 className="text-2xl font-bold">Pull Requests</h2>
              <p className="text-gray-400 text-sm mt-1">All pull requests from connected repos</p>
            </div>
            <PRTable prs={prs} />
          </div>
        )}

        {/* Leaderboard Tab */}
        {activeTab === 'leaderboard' && (
          <div>
            <div className="mb-6">
              <h2 className="text-2xl font-bold">Leaderboard</h2>
              <p className="text-gray-400 text-sm mt-1">Developers ranked by PR activity</p>
            </div>
            <div className="bg-gray-900 rounded-xl border border-gray-800">
              <div className="divide-y divide-gray-800">
                {leaderboard.map((dev, index) => (
                  <div key={dev.githubLogin} className="flex items-center gap-4 p-4 hover:bg-gray-800 transition">
                    <span className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold flex-shrink-0 ${
                      index === 0 ? 'bg-yellow-500 text-black' :
                      index === 1 ? 'bg-gray-400 text-black' :
                      index === 2 ? 'bg-orange-600 text-white' :
                      'bg-gray-800 text-gray-400'
                    }`}>
                      {index + 1}
                    </span>
                    <img src={dev.avatarUrl} alt={dev.githubLogin} className="w-9 h-9 rounded-full" />
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
        )}

      </div>
    </div>
  )
}

export default App