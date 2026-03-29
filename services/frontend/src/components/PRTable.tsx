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

interface PRTableProps {
  prs: PR[]
}

export default function PRTable({ prs }: PRTableProps) {
  const stateColor = (state: string) => {
    if (state === 'open') return 'bg-green-500/20 text-green-400'
    if (state === 'closed') return 'bg-red-500/20 text-red-400'
    return 'bg-purple-500/20 text-purple-400'
  }

  const formatDate = (dateStr: string) => {
    return new Date(dateStr).toLocaleDateString('en-US', {
      month: 'short', day: 'numeric', year: 'numeric'
    })
  }

  return (
    <div className="bg-gray-900 rounded-xl border border-gray-800">
      <div className="p-6 border-b border-gray-800">
        <h2 className="text-xl font-semibold">Pull Requests</h2>
        <p className="text-gray-400 text-sm mt-1">{prs.length} pull requests</p>
      </div>
      <div className="divide-y divide-gray-800">
        {prs.length === 0 && (
          <p className="text-gray-500 p-6 text-center">No pull requests found</p>
        )}
        {prs.map((pr) => (
          <div key={pr.id} className="flex items-start gap-4 p-4 hover:bg-gray-800 transition">
            <img
              src={pr.author?.avatarUrl}
              alt={pr.author?.githubLogin}
              className="w-8 h-8 rounded-full mt-1 flex-shrink-0"
            />
            <div className="flex-1 min-w-0">
              <p className="text-white font-medium truncate">{pr.title}</p>
              <p className="text-gray-500 text-xs mt-1">
                by {pr.author?.githubLogin} · {formatDate(pr.createdAt)}
              </p>
            </div>
            <span className={`text-xs px-2 py-1 rounded-full font-medium flex-shrink-0 ${stateColor(pr.state)}`}>
              {pr.state}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}