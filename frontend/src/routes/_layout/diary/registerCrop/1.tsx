import { createFileRoute } from '@tanstack/react-router';
import classnames from 'classnames/bind';
import type { ChangeEvent } from 'react';
import { useEffect } from 'react';

import LinkButton from '@/components/Buttons/LinkButton';
import Heading from '@/components/Heading';
import LabeledInput from '@/components/Inputs/LabeledInput';
import useRegisterCropStore from '@/store/registerCropStore';

import styles from './registerCrop.module.scss';

const cx = classnames.bind(styles);

export const Route = createFileRoute('/_layout/diary/registerCrop/1')({
  component: GetNicknamePage,
});

function GetNicknamePage() {
  const nickname = useRegisterCropStore(state => state.nickname);
  const setNickname = useRegisterCropStore(state => state.setNickname);
  const resetCropForm = useRegisterCropStore(state => state.resetCropForm);
  const isValid = nickname.length >= 1 && nickname.length <= 30;

  const handleNameChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNickname(e.currentTarget.value);
  };

  useEffect(() => {
    resetCropForm(); // 페이지가 로드될 때 상태 초기화
  }, [resetCropForm]);

  return (
    <div className={cx('registerPage')}>
      <div className={cx('headingContainer')}>
        <Heading>
          내 작물의
          <br />
          별명을 지어주세요.
        </Heading>
      </div>
      <div className={cx('inputContainer')}>
        <LabeledInput
          label="작물 별명"
          placeholder="별명을 입력해주세요"
          onChange={handleNameChange}
          value={nickname}
        />
      </div>
      <div className={cx('buttonContainer')}>
        <LinkButton
          buttonStyle={{ style: 'primary', size: 'large' }}
          to="/diary/registerCrop/2"
          disabled={!isValid} 
        >
          다음
        </LinkButton>
      </div>
    </div>
  );
}

export default GetNicknamePage;