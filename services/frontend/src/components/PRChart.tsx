import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
  Tooltip, ResponsiveContainer
} from 'recharts'

interface PR {
  createdAt: string
  state: string
}

interface PRChartProps {
  prs: PR[]
}

export default function PRChart({ prs }: PRChartProps) {
  const grouped: Record<string, { open: number; closed: number }> = {}

  prs.forEach((pr) => {
    const date = new Date(pr.createdAt).toLocaleDateString('en-US', {
      month: 'short', day: 'numeric'
    })
    if (!grouped[date]) grouped[date] = { open: 0, closed: 0 }
    if (pr.state === 'open') grouped[date].open++
    else grouped[date].closed++
  })

  const data = Object.entries(grouped).map(([date, counts]) => ({
    date,
    open: counts.open,
    closed: counts.closed,
  }))

  return (
    <div className="bg-gray-900 rounded-xl border border-gray-800 p-6">
      <h2 className="text-xl font-semibold mb-1">PR Activity</h2>
      <p className="text-gray-400 text-sm mb-6">Pull requests by date</p>
      <ResponsiveContainer width="100%" height={250}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#374151" />
          <XAxis dataKey="date" tick={{ fill: '#9CA3AF', fontSize: 12 }} />
          <YAxis tick={{ fill: '#9CA3AF', fontSize: 12 }} />
          <Tooltip
            contentStyle={{ backgroundColor: '#111827', border: '1px solid #374151', borderRadius: '8px' }}
            labelStyle={{ color: '#F9FAFB' }}
          />
          <Bar dataKey="open" fill="#3B82F6" radius={[4, 4, 0, 0]} name="Open" />
          <Bar dataKey="closed" fill="#8B5CF6" radius={[4, 4, 0, 0]} name="Closed" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}