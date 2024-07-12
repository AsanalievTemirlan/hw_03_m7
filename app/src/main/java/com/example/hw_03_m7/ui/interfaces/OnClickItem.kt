package com.example.hw_03_m7.ui.interfaces

import com.example.hw_03_m7.data.model.MedicinesModel

interface OnClickItem {


    fun onLongClick(medicinesModel: MedicinesModel)

    fun onClick(medicinesModel: MedicinesModel)
}