import { withAsyncPaginate } from 'react-select-async-paginate'
import { Props as SelectProps } from './Select'
import dynamic from 'next/dynamic'
import { FunctionComponent } from 'react'

const CustomSelect = dynamic(() => import('./Select'), { ssr: false })

const CustomAsyncPaginate = withAsyncPaginate(CustomSelect)

interface Props extends SelectProps {
  options: Array<{ label: string; value: string }>
  loadOptions: (
    value,
    loadedOptions,
  ) => { options: Array<{ label: string; value: string }>; hasMore: boolean }
}

const AutocompleteSelect: FunctionComponent<Props> = (props) => {
  return <CustomAsyncPaginate {...props} debounceTimeout={300} cacheOptions />
}

export default AutocompleteSelect
