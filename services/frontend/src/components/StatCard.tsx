interface StatCardProps {
  title: string
  value: string | number
  subtitle?: string
  color?: 'white' | 'green' | 'red' | 'blue'
}

export default function StatCard({ title, value, subtitle, color = 'white' }: StatCardProps) {
  const colorMap = {
    white: 'text-white',
    green: 'text-green-400',
    red: 'text-red-400',
    blue: 'text-blue-400',
  }

  return (
    <div className="bg-gray-900 rounded-xl p-6 border border-gray-800 hover:border-gray-700 transition">
      <p className="text-gray-400 text-sm">{title}</p>
      <p className={`text-3xl font-bold mt-1 ${colorMap[color]}`}>{value}</p>
      {subtitle && <p className="text-gray-500 text-xs mt-1">{subtitle}</p>}
    </div>
  )
}