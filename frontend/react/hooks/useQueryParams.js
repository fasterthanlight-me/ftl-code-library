// this hook is used to get/set/remove GET params in current page url

import { createSearchParams, useLocation, useNavigate } from 'react-router-dom'

export const useQueryParams = () => {
  const navigate = useNavigate()
  const location = useLocation()
  const params = new URLSearchParams(location.search)
  const queryParams = Object.fromEntries(params)
  const setQueryParams = (newParams) => {
    const searchParamStr = `?${createSearchParams({ ...queryParams, ...newParams })}`
    if (location.search !== searchParamStr) {
      navigate({
        pathname: location.pathname,
        search: searchParamStr,
      })
    }
  }
  const removeQueryParam = (param) => {
    const currentParam = { ...queryParams }
    delete currentParam[param]
    navigate({
      pathname: location.pathname,
      search: `?${createSearchParams(currentParam)}`,
    })
  }
  return { queryParams, setQueryParams, removeQueryParam }
}
