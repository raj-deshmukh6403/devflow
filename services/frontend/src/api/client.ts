const BASE_URL = import.meta.env.VITE_API_URL 
  ? `${import.meta.env.VITE_API_URL}/api`
  : '/api'

export async function fetchSummary() {
  const res = await fetch(`${BASE_URL}/metrics/summary`)
  return res.json()
}

export async function fetchLeaderboard() {
  const res = await fetch(`${BASE_URL}/developers/leaderboard`)
  return res.json()
}

export async function fetchAllPRs() {
  const from = '2024-01-01T00:00:00Z'
  const to = new Date().toISOString()
  const res = await fetch(`${BASE_URL}/metrics/prs?from=${from}&to=${to}`)
  return res.json()
}

export async function fetchBuilds(from: string, to: string) {
  const res = await fetch(`${BASE_URL}/metrics/builds?from=${from}&to=${to}`)
  return res.json()
}

//phase 4 done