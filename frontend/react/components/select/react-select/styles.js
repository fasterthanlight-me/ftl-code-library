import colors from 'assets/styles/modules/colors.module.scss'

const customStyles = {
  container: (provided, state) => {
    return {
      ...provided,
      minWidth: 130,
      height: 'fit-content',
      zIndex: 10,
    }
  },
  control: (provided) => {
    return {
      ...provided,
      paddingRight: '1rem',
      borderRadius: '0.4rem',
      cursor: 'pointer',
      boxShadow: 'none',
      borderColor: colors.borderLightGray,
      '&:hover': {
        borderColor: colors.dark,
      },
    }
  },
  option: (provided, state) => {
    const fontWeight = 'normal'
    const color = state.isSelected ? colors.green : colors.mediumGray
    const backgroundColor = colors.white
    return {
      ...provided,
      fontWeight,
      color,
      backgroundColor,
      cursor: 'pointer',
      '&:hover': {
        backgroundColor: '#F8F7FC',
        color: colors.dark,
      },
      padding: '0.6rem 1rem',
    }
  },
  valueContainer: (provided) => ({
    ...provided,
    padding: '0.6rem 1rem',
  }),
  placeholder: (provided) => ({
    ...provided,
    color: colors.dark,
    fontSize: '1.6rem',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    width: '100%',
    overflow: 'hidden',
    textAlign: 'left',
  }),
  indicatorSeparator: (provided) => ({
    ...provided,
    display: 'none',
  }),
  dropdownIndicator: (provided, state) => {
    const transform =
      state.isFocused || state.isActive ? 'rotate(180deg)' : null
    const transition = 'transform 300ms ease-out, fill 500ms ease-out'
    return {
      ...provided,
      transform,
      transition,
      '&>svg': {
        width: '1.3rem',
        height: '0.9rem',
        fill: state.isFocused ? colors.primary : colors.mediumGray,
      },
    }
  },
  menu: (provided) => {
    return {
      ...provided,
      width: 'max-content',
      minWidth: '100%',
      maxHeight: '31rem',
      padding: 0,
      overflowY: 'hidden',
    }
  },
  menuList: (provided) => {
    return {
      ...provided,
      paddingRight: '0.5rem',
    }
  },
  menuPortal: (provided) => ({
    ...provided,
    zIndex: 1000,
  }),
}

export default customStyles
