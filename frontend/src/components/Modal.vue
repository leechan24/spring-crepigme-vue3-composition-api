<template>
    <div class="modal">
        <div class="modal-dialog">
            <div class="modal-content" :style="{width: width, 'max-width' : maxWidth} " >
            <slot name="modal-header"></slot>
            <slot name="modal-body"></slot>
            <slot name="modal-footer"></slot>
            </div>
        </div>
    </div>
</template>

<script>
import { defineComponent } from '@vue/composition-api';

export default defineComponent({
    props: {
        width: {
            type: String,
            default: 'auto',
        },
        maxWidth: {
            type: String,
            default: 'none',
        },
    },
});
</script>

<style lang="scss" scoped>
.modal {
    position: fixed;
    top : 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 99999;
    &.modal-container-delete {
        .modal-dialog .modal-content {
            position: absolute;
            top: 5%;
            animation: fadeTop 0.3s linear 0.1s both;
        }
    }

    .modal-dialog {
        display: flex;
        justify-content: center;
        align-items: center;
        width : 100%;
        height: 100%;

        .modal-content {
            position: relative;
            display: flex;
            flex-direction: column;
        }
    }
}
@keyframes fadeTop { 
    0% {
        transform: translateY(-10px);
        opacity: 0;
    }
    100% {
        transform: translateY(0px);
        opacity: 1;
    } 
}
</style>